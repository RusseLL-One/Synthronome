package com.one.russell.metroman_20.domain.usecases

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.one.russell.metroman_20.domain.Constants.CLICK_WORKER_TAG

class StopClickingUseCase(
    private val appContext: Context
) {
    fun execute() = WorkManager
        .getInstance(appContext)
        .cancelAllWorkByTag(CLICK_WORKER_TAG)
}