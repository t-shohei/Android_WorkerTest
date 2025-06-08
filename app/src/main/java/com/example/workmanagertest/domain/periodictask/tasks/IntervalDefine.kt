package com.example.workmanagertest.domain.periodictask.tasks


sealed interface IntervalDefine : PeriodicTask {
    val intervalMinute: Long
}