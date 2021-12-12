package com.example.downloading.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.rxjava3.RxWorker
import com.example.downloading.R
import com.example.downloading.repository.DownloadingRepositoryImpl
import okhttp3.ResponseBody
import java.io.*
import com.example.downloading.model.DownloadModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.pow
import kotlin.math.roundToInt


const val ARRAY_SIZE = 1024

private const val NOTIFICATION_ID: Int = 500
private const val NOTIFICATION_CHANNEL: String = "Notification_C"

class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val downloadRepo = DownloadingRepositoryImpl()
    private val notificationBuilder =
        NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
    override fun createWork(): Single<Result> {
        return Single.fromObservable(downloadRepo.download()).map { response ->
            saveFile(response.body())
        }.subscribeOn(Schedulers.io())
            .map {
                notificationManager.cancel(NOTIFICATION_ID)
                Result.success()
            }
            .onErrorReturn { Result.failure() }
    }
    @SuppressLint("SdCardPath")
    fun saveFile(body: ResponseBody?) {
        createNotificationChannel()
        displayNotification(DownloadModel("Please wait...", 0, 0,0))
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED != state) {
            return
        }
        var totalFileSize: Int
        var count: Int
        val data = ByteArray(ARRAY_SIZE)
        val fileSize = body?.contentLength()
        Log.d("fileSize Total",fileSize.toString())
        Log.d(" Total",(fileSize?.div(1024)).toString())

        val input: InputStream = BufferedInputStream(body?.byteStream(), 1024 * 8)
        val output: OutputStream = FileOutputStream("/sdcard/myfile_${System.currentTimeMillis()}.mp4")
        var total = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        totalFileSize = (fileSize?.div(1024.0.pow(2.0)))?.toInt() ?: 0
        Log.d(" totalFileSize",totalFileSize.toString())

        while (input.read(data).also { count = it } != -1) {
            total += count
            val current = (total / 1024.0.pow(2.0)).roundToInt().toDouble()
            val currentTime = System.currentTimeMillis() - startTime
//            val download = DownloadModel()
//            download.totalFileSize = totalFileSize
            if (currentTime > 1000 * timeCount) {
//                download.currentFileSize = current.toInt()
//                download.proggress = progress
                val progress = ((total * 100) / (fileSize ?: 0L)).toInt()

                Log.d("Download Proggress",progress.toString())

                displayNotification(DownloadModel(message = "name",proggress = progress,currentFileSize = current.toInt(),
                    totalFileSize = totalFileSize))
                timeCount++
            }
            output.write(data, 0, count)
        }
        output.flush()
        output.close()
        input.close()

    }
    private fun createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("RemoteViewLayout")
    private fun displayNotification(downloadModel: DownloadModel) {
        val remoteView = RemoteViews(applicationContext.packageName, R.layout.custom_notif)
        remoteView.setImageViewResource(R.id.iv_notif, R.drawable.ic_launcher_background)
        remoteView.setTextViewText(R.id.tv_notif_progress, "${downloadModel.message} (${downloadModel.proggress}/${downloadModel.totalFileSize} complete)")
        remoteView.setTextViewText(R.id.tv_notif_title, "Downloading Images")
        remoteView.setProgressBar(R.id.pb_notif, downloadModel.totalFileSize, downloadModel.proggress, false)
        notificationBuilder
            .setContent(remoteView)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }


    override fun onStopped() {
        super.onStopped()
        notificationManager.cancel(NOTIFICATION_ID)
    }

}


