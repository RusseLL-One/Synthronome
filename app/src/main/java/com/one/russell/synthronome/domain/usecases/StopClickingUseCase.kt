package com.one.russell.synthronome.domain.usecases

import android.content.Context
import androidx.work.WorkManager
import com.one.russell.synthronome.domain.workers.ClickWorker.Companion.CLICK_WORKER_TAG

class StopClickingUseCase(
    private val appContext: Context
) {
    fun execute() = WorkManager
        .getInstance(appContext)
        .cancelAllWorkByTag(CLICK_WORKER_TAG)
}