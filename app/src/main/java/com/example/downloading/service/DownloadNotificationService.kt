package com.example.downloading.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.downloading.R
import com.example.downloading.base.GlobalConstants
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.example.downloading.ui.activity.DownloadActivity


class DownloadNotificationService : Service() {

    val context = Context.NOTIFICATION_SERVICE
//    var notificationManager = NotificationManagerCompat.from(DownloadNotificationService::)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
//
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        notificationBuilder = NotificationCompat.Builder(this)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("Download")
//            .setContentText("Downloading File")
//            .setAutoCancel(true)
//        notificationManager?.notify(0, notificationBuilder?.build())
//


        val notificationIntent = Intent(this, DownloadActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val progress = intent?.getIntExtra(GlobalConstants.PROGRESS_SERVICE,0)
        Log.d(DownloadNotificationService::class.java.simpleName,progress.toString())

        val progressMax = 100
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, GlobalConstants.CHANNEL_ID)
            .setContentTitle("Download")
            .setContentText("Download in progress ")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setProgress(progressMax, 0, false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
        startForeground(1, notification.build())

        Thread.sleep(2000)
        for ( p in 0..progressMax step 25) {
            notification.setProgress(progressMax, p, false)
//            notificationManager.notify(1,notification.build())
            startForeground(1, notification.build())
            Thread.sleep(2000)
        }
        notification.setContentText("Download Finished")
            .setProgress(0,0,false)
            .setOngoing(false)
        startForeground(1, notification.build())

//        notificationManager.notify(1,notification.build())
//        startForeground(1, notificationManager)

//        startForeground(0, notificationBuilder?.build())
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
        }
    }


}