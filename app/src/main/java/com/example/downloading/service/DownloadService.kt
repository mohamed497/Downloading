package com.example.downloading.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.downloading.DownloadActivity
import com.example.downloading.R
import com.example.downloading.base.GlobalConstants
import java.util.*
import kotlin.concurrent.schedule


class DownloadService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                createNotification()

        val notificationIntent = Intent(this, DownloadActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )


        val progressMax = 100

        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, GlobalConstants.CHANNEL_ID)
            .setContentTitle("Download")
            .setContentText("Download in progress ")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setProgress(progressMax, 0, false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)


            for ( progress in 1..100 step 10){
                Timer().schedule(1000){
                    notification.setProgress(progressMax, progress, false)
                }

            }
            notification.setContentText("Download Finished")
                .setProgress(0,0,false)
                .setOngoing(false)


        startForeground(1, notification.build())


        return START_NOT_STICKY
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                GlobalConstants.CHANNEL_ID,
                "Example Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }    }

}