package com.example.workmanagertest.data.repositry.factory

import android.content.Context
import com.example.workmanagertest.data.repositry.imple.DefaultTaskRepository
import com.example.workmanagertest.data.repositry.TaskRepository

object RepositoryFactory {
    fun createTaskRepository(context: Context): TaskRepository {
        return DefaultTaskRepository(context.getSharedPreferences("PERIODICTASK", Context.MODE_PRIVATE))
    }
}