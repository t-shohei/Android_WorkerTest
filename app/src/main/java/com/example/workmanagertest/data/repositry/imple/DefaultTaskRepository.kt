package com.example.workmanagertest.data.repositry.imple

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.workmanagertest.data.repositry.TaskRepository
import com.example.workmanagertest.domain.periodictask.tasks.PeriodicTask
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.decodeFromStream
import kotlin.reflect.full.createInstance

class DefaultTaskRepository(private val pref: SharedPreferences) : TaskRepository {
    override suspend fun getTasks(key: String): List<PeriodicTask> {
        val a = pref.getString(key, null) ?: return emptyList()
        val b = Json.decodeFromString<List<String>>(a)

        val c = b.map {
            val kClass = Class.forName(it).kotlin
            kClass.objectInstance as? PeriodicTask ?: kClass.createInstance() as? PeriodicTask
            ?: return emptyList()
        }
//        val c = b.map { createTaskInstance(it) ?: return emptyList()}
        return c
    }

    override suspend fun putTasks(key: String, data: List<PeriodicTask>) {
        pref.edit() {
            val classNames = data.map { it::class.qualifiedName }
            val tasks = Json.encodeToString(classNames)
            putString(key, tasks)
        }
    }

    override suspend fun deleteAllTasks() {
        pref.all.keys.forEach {
            deleteTask(it)
        }
    }

    override suspend fun deleteTask(key: String) {
        pref.edit(commit = true) { remove(key) }
    }
}