package com.example.workmanagertest.domain.usecase

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.workmanagertest.data.repositry.TaskRepository
import com.example.workmanagertest.domain.periodictask.tasks.IntervalDefine
import com.example.workmanagertest.domain.periodictask.tasks.PeriodicTask
import com.example.workmanagertest.domain.worker.TestWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class TestWorkBuilder(private val context: Context, private val repository: TaskRepository):PeriodicUseCase {
    override fun invoke() {
        // 前回登録していたワーカー削除
        WorkManager.getInstance(context)
            .cancelAllWorkByTag("com.example.workmanagertest.domain.worker.TestWorker")
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("TestWorkBuilder", "delete Tasks")
            repository.deleteAllTasks()
        }
        // 定期実行間隔、時間帯指定処理クラスのリストを取得
        val testSealedWorkList = PeriodicTask.getSubClasses<IntervalDefine>(context)
        Log.d("TestWorkBuilder", "testSealedWorkList: $testSealedWorkList")

        // 定期実行間隔でグループ化、ワークリクエストの作成
        val grouped = testSealedWorkList.groupBy { it.intervalMinute }
        grouped.forEach { (interval, list) ->
            val classNames = list.map { it::class.qualifiedName }
            Log.d("TestWorkBuilder", "classNames: $classNames")

            val json = Json.encodeToString(classNames)
            Log.d("TestWorkBuilder", "json: $json")

            val tag = "${TestWorker.PREFIX}_$interval"
            Log.d("TestWorkBuilder", "tag: $tag")

            createWorkRequest(interval = interval, list = json, tag = tag)
        }
    }

    private fun createWorkRequest(interval: Long, list: String, tag: String) {
        val data = Data.Builder().putString("data", list).build()
        Log.d("TestWorkBuilder", "data: $data")
        // リクエスト作成
        PeriodicWorkRequestBuilder<TestWorker>(
            interval, TimeUnit.MINUTES
        ).apply {
            setInputData(data)
            setInitialDelay(0, TimeUnit.MILLISECONDS)
            setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
        }.build().let {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                tag,
                ExistingPeriodicWorkPolicy.UPDATE,
                it
            )
        }
    }

}
