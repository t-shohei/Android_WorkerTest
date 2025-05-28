package com.example.workmanagertest.periodictask

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.workmanagertest.periodictask.tasks.IntervalDefine
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class TestWorkBuilder(private val context: Context) {
    operator fun invoke() {
        // 前回登録していたワーカー削除
        WorkManager.getInstance(context)
            .cancelAllWorkByTag("com.example.workmanagertest.periodictask.TestWorker")

        // 定期実行間隔、時間帯指定処理クラスのリストを取得
        val testSealedWorkList = IntervalDefine.getSubClasses()
        Log.d("TestWorkBuilder", "testSealedWorkList: $testSealedWorkList")

        // 定期実行間隔でグループ化、ワークリクエストの作成
        val grouped = testSealedWorkList.groupBy { it.intervalTime }
        grouped.forEach { (interval, list) ->
            val classNames = list.map { it::class.qualifiedName!! }
            Log.d("TestWorkBuilder", "classNames: $classNames")


            val json = Json.encodeToString(classNames)
            Log.d("TestWorkBuilder", "json: $json")


            val tag = "${TestWorker.PREFIX}_$interval"
            Log.d("TestWorkBuilder", "tag: $tag")


            val data = Data.Builder().putString("data", json).build()
            Log.d("TestWorkBuilder", "data: $data")

            // リクエスト作成
            PeriodicWorkRequestBuilder<TestWorker>(
                interval, TimeUnit.MINUTES
            ).apply {
                setInputData(data)
                setInitialDelay(0, TimeUnit.MILLISECONDS)
                setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
            }.build().let {
                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    tag,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    it
                )
            }
        }
    }
}
