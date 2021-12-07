package com.example.downloading

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.downloading.base.GlobalConstants.PERMISION_REQUEST
import com.example.downloading.base.RunTimePermission
import com.example.downloading.repository.DownloadingRepositoryImpl
import com.example.downloading.service.DownloadService
import com.example.downloading.worker.DownloadWorker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_download.*
import java.util.concurrent.TimeUnit

class DownloadActivity : AppCompatActivity() {
    private var runtimePermission: RunTimePermission = RunTimePermission(this)
    private val workManager = WorkManager.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

//        val downloadRepo = DownloadingRepositoryImpl()
//        downloadRepo.download().observeOn(AndroidSchedulers.mainThread())
//        downloadRepo.download().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()

        btnStartDownloadWork.setOnClickListener { view ->
            when (view.id) {
                R.id.btnStartDownloadWork -> {
                    runtimePermission.requestPermission(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        object : RunTimePermission.PermissionCallback {
                            override fun onGranted() {

                                StartOneTimeWorkManager()
                            }

                            override fun onDenied() {
                                Toast.makeText(
                                    this@DownloadActivity,
                                    "Need Permission of Storage",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }

            }
        }
    }

    private fun StartOneTimeWorkManager() {

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val task =
            OneTimeWorkRequest.Builder(DownloadWorker::class.java).setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        workManager.enqueue(task)
        workManager.getWorkInfoByIdLiveData(task.id)
            .observe(this@DownloadActivity, Observer { work ->

                work?.let {
                    if (work.state == WorkInfo.State.RUNNING) {
                        Log.d(DownloadActivity::javaClass.name, "RUNNING")
                        loaderShow(true)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(Intent(
                                applicationContext,
                                DownloadService::class.java
                            ))
                        }

                    } else
                        if (work.state.isFinished) {
                            Log.d(DownloadActivity::javaClass.name, "DONE")
                            Toast.makeText(
                                this@DownloadActivity,
                                getString(R.string.work_done),
                                Toast.LENGTH_SHORT
                            ).show()
                            loaderShow(false)
                        }
                }
            })
    }


    private fun loaderShow(flag: Boolean) {
        when (flag) {
            true -> llProgress.visibility = View.VISIBLE
            false -> llProgress.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISION_REQUEST)
            runtimePermission.onRequestPermissionsResult(grantResults)

    }

    override fun onPause() {
        stopService(Intent(this, DownloadService::class.java))
        super.onPause()
    }
}