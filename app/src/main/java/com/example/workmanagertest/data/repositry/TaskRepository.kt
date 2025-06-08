package com.example.workmanagertest.data.repositry

import com.example.workmanagertest.domain.periodictask.tasks.PeriodicTask

interface TaskRepository {
    suspend fun getTasks(key:  String): List<PeriodicTask>
    suspend fun putTasks(key: String, data: List<PeriodicTask>)
    suspend fun deleteAllTasks()
    suspend fun deleteTask(key: String)
}