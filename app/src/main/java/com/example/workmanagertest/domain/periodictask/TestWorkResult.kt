package com.example.workmanagertest.domain.periodictask

import kotlinx.serialization.Serializable
@Serializable
sealed class TestWorkResult {
    data object Success : TestWorkResult()
    data object Retry : TestWorkResult()
    data object Failure : TestWorkResult()
}