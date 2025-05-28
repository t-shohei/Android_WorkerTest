package com.example.workmanagertest.data.repositry

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.example.workmanagertest.periodictask.tasks.TestSealed

interface TaskRepository {
    suspend fun getTasks(key:  String): List<TestSealed>
    suspend fun putTasks(key: String, data: List<TestSealed>)
    suspend fun deleteAllTasks()
    suspend fun deleteTask(key: String)
}