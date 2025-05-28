package com.example.workmanagertest.periodictask.tasks

import android.content.Context
import com.example.workmanagertest.periodictask.TestWorkResult
import kotlinx.serialization.Serializable
import kotlin.reflect.full.createInstance

@Serializable
sealed interface TestSealed {
    val timeoutMillis: Long
    suspend operator fun invoke(context: Context): TestWorkResult
}