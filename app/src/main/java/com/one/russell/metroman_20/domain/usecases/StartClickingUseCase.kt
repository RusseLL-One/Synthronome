package com.one.russell.metroman_20.domain.usecases

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.one.russell.metroman_20.domain.Constants.CLICK_WORKER_TAG
import com.one.russell.metroman_20.domain.workers.ClickWorker

class StartClickingUseCase(
    private val appContext: Context
) {
    fun execute() {
        OneTimeWorkRequestBuilder<ClickWorker>()
            .addTag(CLICK_WORKER_TAG)
            .build()
            .also {
                WorkManager
                    .getInstance(appContext)
                    .enqueue(it)
            }
    }
}