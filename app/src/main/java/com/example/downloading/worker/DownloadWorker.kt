package com.example.downloading.worker
import android.content.Context
import android.os.Environment
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.example.downloading.repository.DownloadingRepositoryImpl
import io.reactivex.Single
import okhttp3.ResponseBody
import java.io.*
import com.example.downloading.model.Download
import io.reactivex.schedulers.Schedulers
import kotlin.math.pow

val downloadRepo = DownloadingRepositoryImpl()
private var totalFileSize = 0

class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {

    override fun createWork(): Single<Result> {
        return Single.fromObservable(downloadRepo.download()).map { response ->
            downloadFile(response.body())
        }.subscribeOn(Schedulers.io())
            .map { Result.success() }
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
//    onDownloadComplete()
    output.flush()
    output.close()
    bis.close()
}
