package com.example.workmanagertest.domain.usecase

import android.content.Context
import com.example.workmanagertest.data.repositry.factory.RepositoryFactory
import com.example.workmanagertest.domain.periodictask.TestWorkResult

object UseCaseFactory {
    fun createTestWorkerBuilder(context: Context): PeriodicUseCase{
        return TestWorkBuilder(context = context, repository = RepositoryFactory.createTaskRepository(context))
    }
}