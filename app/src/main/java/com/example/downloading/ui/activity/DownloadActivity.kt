package com.example.downloading.ui.activity

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
import com.example.downloading.service.DownloadNotificationService
import com.example.downloading.worker.DownloadWorker
import kotlinx.android.synthetic.main.activity_download.*
import java.util.concurrent.TimeUnit

import com.example.downloading.base.GlobalConstants
import androidx.work.WorkInfo
import com.example.downloading.R


class DownloadActivity : AppCompatActivity() {
    private var runtimePermission: RunTimePermission = RunTimePermission(this)
    private val workManager = WorkManager.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        startDownloadBtn()
    }
    private fun startDownloadBtn(){
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
                        val getProgress = work.progress.getInt(GlobalConstants.PROGRESS_WORK, 1)
                        progress.progress = getProgress

                        Log.d(DownloadActivity::javaClass.name, "RUNNING")
                        loaderShow(true)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val serviceIntent = Intent(this,DownloadNotificationService::class.java)
                                        serviceIntent.putExtra(GlobalConstants.PROGRESS_SERVICE,getProgress)
                            startForegroundService(serviceIntent)
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
        stopService(Intent(this, DownloadNotificationService::class.java))
        super.onPause()
    }

}