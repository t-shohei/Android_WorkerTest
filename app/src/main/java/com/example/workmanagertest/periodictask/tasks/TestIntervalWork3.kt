package com.example.workmanagertest.periodictask.tasks

import android.content.Context
import com.example.workmanagertest.periodictask.TestWorkResult
import kotlinx.serialization.Serializable


class TestIntervalWork3() : IntervalDefine {
    override val intervalTime: Long = 20L
    override val timeoutMillis: Long = 1000L
    override suspend fun invoke(context: Context): TestWorkResult {
        return TestWorkResult.Retry
    }
}

