package com.example.workmanagertest.dto

import com.example.workmanagertest.periodictask.tasks.TestSealed
import kotlinx.serialization.Serializable

@Serializable
data class TestWorkListDto(
    val workList: List<TestSealed>
)