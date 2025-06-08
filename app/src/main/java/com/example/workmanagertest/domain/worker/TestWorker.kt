package com.example.workmanagertest.domain.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workmanagertest.data.repositry.factory.RepositoryFactory
import com.example.workmanagertest.data.repositry.TaskRepository
import com.example.workmanagertest.domain.periodictask.TestWorkResult
import com.example.workmanagertest.domain.periodictask.tasks.PeriodicTask
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.Json
import kotlin.reflect.full.createInstance

class TestWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    private val repository: TaskRepository = RepositoryFactory.createTaskRepository(context)

    override suspend fun doWork(): Result {
        Log.d("TestWorker", "doWork()")

        val tagName = tags.toString()
        Log.d("TestWorker", "tagName: $tagName")

        val jsonStringList = inputData.getString("data") ?: return Result.failure()
        Log.d("TestWorker", "jsonStringList: $jsonStringList")

        val classNames = Json.decodeFromString<List<String>>(jsonStringList)
        Log.d("TestWorker", "classNames: $classNames")

        val previousRetryTasks = repository.getTasks(tagName)
        Log.d("TestWorker", "previousRetryTasks: $previousRetryTasks")

        val tasks = previousRetryTasks.ifEmpty {
            classNames.mapNotNull {
                createTaskInstance(it)
            }
        }
        Log.d("TestWorker", "tasks: $tasks")
        val retryList = mutableListOf<PeriodicTask>()
        tasks.forEach {
            withTimeoutOrNull(it.timeoutMillis) {
                val taskResult = it(context)
                if (taskResult == TestWorkResult.Retry) retryList.add(it)
            }
        }
        Log.d("TestWorker", "retryList $retryList")


        return if (retryList.isEmpty()) {
            repository.deleteTask(tagName)
            Log.d("TestWorker", "Result success")
            Result.success()
        } else {
            if (runAttemptCount > 2) {
                Log.d("TestWorker", "Result failure")
                repository.deleteTask(tagName)
                Result.failure()
            } else {
                Log.d("TestWorker", "Result Retry")
                repository.putTasks(tagName, retryList.toList())
                Result.retry()
            }
        }
    }

    private fun createTaskInstance(className: String): PeriodicTask? {
        return runCatching {
            val kClass = Class.forName(className).kotlin
            kClass.objectInstance as? PeriodicTask ?: kClass.createInstance() as? PeriodicTask
        }.onFailure {
            Log.e("TestWorker", "クラス生成に失敗: $className", it)
        }.getOrNull()
    }

    companion object {
        const val PREFIX = "periodicTask_"
    }
}
