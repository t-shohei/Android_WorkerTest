package com.example.workmanagertest.domain.periodictask.tasks

import android.content.Context
import com.example.workmanagertest.domain.periodictask.TestWorkResult
import kotlinx.serialization.Serializable


class TestIntervalWork3() : IntervalDefine {
    override val intervalMinute: Long = 20L
    override val timeoutMillis: Long = 1000L
    override suspend fun invoke(context: Context): TestWorkResult {
        return TestWorkResult.Retry
    }
}

