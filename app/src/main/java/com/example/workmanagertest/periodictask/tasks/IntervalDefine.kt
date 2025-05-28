package com.example.workmanagertest.periodictask.tasks

import android.content.Context
import com.example.workmanagertest.periodictask.TestWorkResult
import kotlinx.serialization.Serializable
import kotlin.reflect.full.createInstance

@Serializable
sealed interface IntervalDefine :TestSealed {
    val intervalTime: Long

    companion object {
        fun getSubClasses() = IntervalDefine::class.sealedSubclasses.map { it.createInstance() }
    }
}