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
import android.app.NotificationManager
import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.downloading.model.Download


class DownloadNotificationService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    val context = this

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Download")
            .setContentText("Downloading File")
            .setAutoCancel(true)
        notificationManager?.notify(0, notificationBuilder?.build())



        startForeground(0, notificationBuilder?.build())
        return START_NOT_STICKY
    }

    fun sendNotification(download: Download){
//        sendIntent(download)
        notificationBuilder?.setProgress(100,download.proggress ?: 0,false)
        notificationBuilder?.setContentText(String.format("Downloaded (%d/%d) MB",download.currentFileSize?:0
            ,download.totalFileSize?:0))
        notificationManager?.notify(0, notificationBuilder?.build())
    }

//    private fun sendIntent(download: Download) {
//
//        val intent =  Intent(GlobalConstants.MESSAGE_PROGRESS)
//        intent.putExtra("download",download)
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
//
//    }
     fun onDownloadComplete(){
        val download = Download()
        download.proggress = 100
//        sendIntent(download)
        notificationManager?.cancel(0)
        notificationBuilder?.setProgress(0,0,false)
        notificationBuilder?.setContentText("File Downloaded")
        notificationManager?.notify(0, notificationBuilder?.build())
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

    override fun onTaskRemoved(rootIntent: Intent?) {
        notificationManager?.cancel(0)
        super.onTaskRemoved(rootIntent)
    }

}