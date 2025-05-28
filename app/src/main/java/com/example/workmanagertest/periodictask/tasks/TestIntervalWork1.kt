package com.example.workmanagertest.periodictask.tasks

import android.content.Context
import com.example.workmanagertest.periodictask.TestWorkResult
import kotlinx.serialization.Serializable


class TestIntervalWork1 : IntervalDefine {
    override val intervalTime: Long = 15L
    override val timeoutMillis: Long = 1000L
    override suspend fun invoke(context: Context): TestWorkResult {
        return TestWorkResult.Success
    }
}
