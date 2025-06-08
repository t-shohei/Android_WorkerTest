package com.example.workmanagertest.domain.periodictask.tasks

import android.content.Context
import com.example.workmanagertest.domain.periodictask.TestWorkResult
import kotlinx.serialization.Serializable
import kotlin.reflect.full.createInstance

sealed interface PeriodicTask {
    val timeoutMillis: Long
    fun isEnable(context: Context): Boolean = true
    suspend operator fun invoke(context: Context): TestWorkResult

    companion object {
        inline fun <reified T : PeriodicTask> getSubClasses(context: Context) =
            T::class.sealedSubclasses.map { it.createInstance() }.filter { it.isEnable(context) }
    }
}