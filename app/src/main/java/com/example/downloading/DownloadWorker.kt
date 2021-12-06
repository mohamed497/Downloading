package com.example.downloading

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.example.downloading.base.GlobalConstants.URLFILE
import io.reactivex.Observable
import io.reactivex.Single
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL


class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {
    override fun createWork(): Single<Result> {

        val downloadObservable: Observable<String> = Observable.create { sub ->
            try {
                val url = URL(URLFILE)
                val connection = url.openConnection()
                connection.connect()
                val input = BufferedInputStream(url.openStream(), 8192)
                val length  = connection.contentLength

                val output = FileOutputStream("/sdcard/myfile_${System.currentTimeMillis()}.mp4")
                val data = ByteArray(1024)
                sub.onNext("0%")
                var count: Int?
                var total: Long = 0

                while (run {
                        count = input.read(data)
                        count
                    } != -1) {
                    total += count ?: 0
                    //size
                    sub.onNext((total*100/length).toString() + "%")
                    output.write(data, 0, count ?: 0)
                }

                output.flush()
                output.close()
                input.close()

            } catch (e: Exception) {
                sub.onError(e)
            }
            sub.onComplete()
        }

        return Observable.just(downloadObservable)
//            .flatMap { return@flatMap downloadObservable }
            .toList()
            .map { Result.success() }
//            .subscribeOn(Schedulers.io())

    }

}


