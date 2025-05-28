package com.example.workmanagertest.data.repositry

import android.content.Context

object RepositoryFactory {
    fun createTaskRepository(context: Context): TaskRepository{
        return DefaultTaskRepository(context.getSharedPreferences("PERIODICTASK", Context.MODE_PRIVATE))
    }
}