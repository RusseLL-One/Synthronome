package com.one.russell.metroman_20.domain.usecases

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.one.russell.metroman_20.domain.Constants.CLICK_WORKER_TAG
import com.one.russell.metroman_20.domain.workers.ClickWorker

class StartClickingUseCase(
    private val appContext: Context
) {
    fun execute() {
        val workManagerInstance = WorkManager.getInstance(appContext)

        workManagerInstance.cancelAllWorkByTag(CLICK_WORKER_TAG)

        OneTimeWorkRequestBuilder<ClickWorker>()
            .addTag(CLICK_WORKER_TAG)
            .build()
            .also {
                workManagerInstance.enqueue(it)
            }
    }
}