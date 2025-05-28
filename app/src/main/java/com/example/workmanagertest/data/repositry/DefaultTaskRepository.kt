package com.example.workmanagertest.data.repositry

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.workmanagertest.dto.TestWorkListDto
import com.example.workmanagertest.periodictask.tasks.TestSealed
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.Json

class DefaultTaskRepository(private val pref: SharedPreferences):TaskRepository {
    override suspend fun getTasks(key: String): List<TestSealed> {
        val prefString = pref.getString(key, """{"workList":[]}""")?: """{"workList":[]}"""
        return Json.decodeFromString<List<String>>(prefString).workList
    }

    override suspend fun putTasks(key: String, data: List<TestSealed>) {
        pref.edit() {
            val tasks = TestWorkListDto(data)
            putString(key, Json.encodeToString<TestWorkListDto>((tasks)))
        }
    }

    override suspend fun deleteAllTasks() {
        pref.all.keys.forEach {
            deleteTask(it)
        }
    }

    override suspend fun deleteTask(key: String) {
        pref.edit() { remove(key) }
    }
}