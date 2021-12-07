package com.example.downloading.worker
import android.content.Context
import android.os.Environment
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.rxjava3.RxWorker
import androidx.work.workDataOf
import com.example.downloading.repository.DownloadingRepositoryImpl
import okhttp3.ResponseBody
import java.io.*
import com.example.downloading.model.Download
import com.example.downloading.service.DownloadNotificationService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlin.math.pow

val downloadRepo = DownloadingRepositoryImpl()
private var totalFileSize = 0


//class ProgressWorker(context: Context, parameters: WorkerParameters) :
//    Worker(context, parameters) {
//
//    companion object {
//        const val Progress = "Progress"
//        private const val delayDuration = 1L
//    }

//    override fun doWork(): Result {
//        setProgressAsync(Data.Builder().putInt("progress", 0).build())
//        Thread.sleep(1000)
//        setProgressAsync(Data.Builder().putInt("progress", 50).build())
//        Thread.sleep(1000)
//        setProgressAsync(Data.Builder().putInt("progress", 100).build())
//        val firstUpdate = workDataOf(Progress to 0)
//        val lastUpdate = workDataOf(Progress to 100)
//        setProgressAsync(firstUpdate)
//        delay(delayDuration)
//        setProgressAsync(lastUpdate)
//        setProgress(firstUpdate)
//        setProgress(lastUpdate)
//        return Result.success()
//    }
//
//}
class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {

    companion object {
        const val Progress = "Progress"
        private const val delayDuration = 1L
    }

    override fun createWork(): Single<Result> {
        return Single.fromObservable(downloadRepo.download()).map { response ->
            downloadFile(response.body())
        }.subscribeOn(Schedulers.io())
            .map { setProgressAsync(Data.Builder().putInt("progress", 0).build())
                Thread.sleep(1000)
                setProgressAsync(Data.Builder().putInt("progress", 25).build())
                Thread.sleep(1000)
                setProgressAsync(Data.Builder().putInt("progress", 50).build())
                Thread.sleep(1000)
                setProgressAsync(Data.Builder().putInt("progress", 75).build())
                Thread.sleep(1000)
                setProgressAsync(Data.Builder().putInt("progress", 100).build())

                Result.success() }
            .onErrorReturn { Result.failure() }
    }
}

fun downloadFile(body: ResponseBody?) {
    var count: Int
    val data = ByteArray(1024)
    val fileSize = body?.contentLength()
    val bis: InputStream = BufferedInputStream(body?.byteStream(), 1024 * 8)
    val outputFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "file.zip"
    )
    val output: OutputStream = FileOutputStream(outputFile)
    var total = 0
    val startTime = System.currentTimeMillis()
    var timeCount = 1
    while (bis.read(data).also { count = it } != -1) {
        total += count
        totalFileSize = (fileSize?.div(1024.0.pow(2.0)))?.toInt() ?: 0
        val current = Math.round(total / 1024.0.pow(2.0)).toDouble()
        val progress = (total * 100 / (fileSize ?: 0L)).toInt()
        val currentTime = System.currentTimeMillis() - startTime
        val download = Download()
        download.totalFileSize = totalFileSize
        if (currentTime > 1000 * timeCount) {
            download.currentFileSize = current.toInt()
            download.proggress = progress
//            sendNotification(download)
            timeCount++
        }
        output.write(data, 0, count)
    }

    output.flush()
    output.close()
    bis.close()

}
