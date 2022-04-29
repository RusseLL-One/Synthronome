package com.one.russell.metroman_20.domain.usecases

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.one.russell.metroman_20.domain.workers.ClickWorker
import com.one.russell.metroman_20.domain.workers.ClickWorker.Companion.CLICK_WORKER_ARG_BPM
import com.one.russell.metroman_20.domain.workers.ClickWorker.Companion.CLICK_WORKER_TAG

class StartClickingUseCase(
    private val appContext: Context
) {
    fun execute(startBpm: Int? = null) {
        val workManagerInstance = WorkManager.getInstance(appContext)

        workManagerInstance.cancelAllWorkByTag(CLICK_WORKER_TAG)

        OneTimeWorkRequestBuilder<ClickWorker>()
            .addTag(CLICK_WORKER_TAG)
            .setInputData(workDataOf(CLICK_WORKER_ARG_BPM to startBpm))
            .build()
            .also {
                workManagerInstance.enqueue(it)
            }
    }
}