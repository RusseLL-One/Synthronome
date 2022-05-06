package com.one.russell.synthronome.domain.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.one.russell.synthronome.domain.ClickState
import com.one.russell.synthronome.domain.notifications.createClickerNotification
import com.one.russell.synthronome.domain.providers.ClickStateProvider
import com.one.russell.synthronome.domain.wrappers.Clicker
import kotlinx.coroutines.awaitCancellation

class ClickWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val clicker: Clicker,
    private val clickStateProvider: ClickStateProvider
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val startBpm = inputData.getInt(CLICK_WORKER_ARG_BPM, 0).takeIf { it != 0 }
            clicker.start(startBpm)
            clickStateProvider.setClickState(ClickState.STARTED)
            setForeground(createForegroundInfo())
            awaitCancellation()
        } catch (e: Exception) {
            Result.failure()
        } finally {
            clicker.stop()
            clickStateProvider.setClickState(ClickState.IDLE)
        }
    }

    private fun createForegroundInfo() = ForegroundInfo(
        CLICK_WORKER_NOTIFICATION_ID,
        createClickerNotification(applicationContext, id)
    )

    companion object {
        const val CLICK_WORKER_ARG_BPM = "CLICK_WORKER_ARG_BPM"
        const val CLICK_WORKER_TAG = "CLICK_WORKER_TAG"
        const val CLICK_WORKER_NOTIFICATION_ID = 94837 // just random number
    }
}
