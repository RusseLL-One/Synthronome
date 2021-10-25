package com.one.russell.metroman_20.domain.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.Constants.CLICK_WORKER_NOTIFICATION_ID
import com.one.russell.metroman_20.domain.notifications.createClickerNotification
import com.one.russell.metroman_20.domain.providers.ClickStateProvider
import com.one.russell.metroman_20.domain.wrappers.Clicker
import kotlinx.coroutines.awaitCancellation

class ClickWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val clicker: Clicker,
    private val clickStateProvider: ClickStateProvider
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            clicker.start()
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
}
