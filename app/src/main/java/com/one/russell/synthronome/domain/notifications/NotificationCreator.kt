package com.one.russell.synthronome.domain.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.one.russell.synthronome.R
import java.util.*

private const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"

fun createClickerNotification(appContext: Context, workerId: UUID): Notification {
    val notificationTitle = appContext.getString(R.string.notification_title)
    val notificationText = appContext.getString(R.string.notification_text)

    val cancelIntent = WorkManager.getInstance(appContext)
        .createCancelPendingIntent(workerId)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createChannel(appContext)
    }

    return NotificationCompat.Builder(appContext, NOTIFICATION_CHANNEL_ID)
        .setTicker(notificationTitle)
        .setContentTitle(notificationTitle)
        .setContentText(notificationText)
        .setSmallIcon(R.drawable.icon_240)
        .setSilent(true)
        .setOngoing(true)
        .setContentIntent(cancelIntent)
        .build()
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createChannel(appContext: Context) {
    val channelName = appContext.getString(R.string.notification_channel_name)
    val channelDescription = appContext.getString(R.string.notification_channel_description)
    val importance = NotificationManager.IMPORTANCE_HIGH

    val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance).apply {
        description = channelDescription
    }

    (appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .run { createNotificationChannel(mChannel) }
}

