package com.udacity.utils

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

private val NOTIFICATION_ID = 0

//Created extension function to send notifications
@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(status: DownloadStatus, title: String, description: String, applicationContext: Context){
    val contentIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        putExtra(INTENT_DOWNLOAD_REPO, title)
        putExtra(INTENT_DOWNLOAD_STATUS, status.toString())
    }

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
    )

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.channel_id)
    )
        .setSmallIcon(R.drawable.github_sign)
        .setContentTitle(title)
        .setContentText(description)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.github_sign,
            applicationContext.getString(R.string.notification_button),
            pendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelNotifications(){
    cancelAll()
}