package com.example.downloading.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.example.downloading.model.DownloadModel


class DownloadActivity : AppCompatActivity() {
    private var runtimePermission: RunTimePermission = RunTimePermission(this)
    private val workManager = WorkManager.getInstance(this)
    lateinit var task: WorkRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        saveDownloadBtn()
    }

    private fun saveDownloadBtn() {
        btnStartDownloadWork.setOnClickListener { view ->
            when (view.id) {
                R.id.btnStartDownloadWork -> {
                    runtimePermission.requestPermission(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        object : RunTimePermission.PermissionCallback {
                            override fun onGranted() {
                                StartDownloadingWorkManager()
                            }

                            override fun onDenied() {
                                makeToast(getString(R.string.work_permission))
                            }
                        })
                }

            }
        }
    }

    private fun StartDownloadingWorkManager() {
        task = setupWorkRequest()
        // make view model - a2l size el function
        workManager.enqueue(task)
        workManager.getWorkInfoByIdLiveData(task.id)
            .observe(this@DownloadActivity, { work ->
                work?.let {
                    if (work.state == WorkInfo.State.RUNNING) {
                        loaderShow(true)
                    } else
                        if (work.state.isFinished) {
                            makeToast(getString(R.string.work_done))
                            loaderShow(false)
                        }
                }
            })
    }

    private fun loaderShow(visibility: Boolean) {
        when (visibility) {
            true -> linearProgress.visibility = View.VISIBLE
            false -> linearProgress.visibility = View.GONE
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

    fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupWorkRequest(): WorkRequest {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        return OneTimeWorkRequest.Builder(DownloadWorker::class.java).setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()
    }
}
