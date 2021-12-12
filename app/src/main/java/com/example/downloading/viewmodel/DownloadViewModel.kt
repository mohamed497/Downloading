package com.example.downloading.viewmodel
//
//import android.content.Context
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.work.*
//import com.example.downloading.worker.DownloadWorker
//import java.util.concurrent.TimeUnit
//
//class DownloadViewModel : ViewModel() {
//
//    private lateinit var workManager: WorkManager
//
//
//    private val downloadMutableLiveData = MutableLiveData<WorkInfo>()
//
//
////    fun getActivityContext(context: Context){
////    }
//
//    fun StartDownloadingWorkManager(context: Context) {
//        workManager = WorkManager.getInstance(context)
//        val constraints =
//            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
//        val task =
//            OneTimeWorkRequest.Builder(DownloadWorker::class.java).setConstraints(constraints)
//                .setBackoffCriteria(
//                    BackoffPolicy.LINEAR,
//                    OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
//                    TimeUnit.MILLISECONDS
//                )
//                .build()
//
//        //
//        //
//        //
//        //
//        //
//        // make view model - a2l size el function
//        workManager.enqueue(task)
//        workManager.getWorkInfoByIdLiveData(task.id)
//            .observe(this, {
//                downloadMutableLiveData.value = it
//            })
////            .observe(this@DownloadActivity, Observer { work ->
////                work?.let {
////                    if (work.state == WorkInfo.State.RUNNING) {
////                        val getProgress = work.progress.getInt(GlobalConstants.PROGRESS_WORK, PROGRESS_DEFAULT_VALUE)
////                        progress.progress = getProgress
////
////                        Log.d(DownloadActivity::javaClass.name, "RUNNING")
////                        loaderShow(true)
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                            val serviceIntent = Intent(this, DownloadNotificationService::class.java)
////                            serviceIntent.putExtra(GlobalConstants.PROGRESS_SERVICE,getProgress)
////                            startForegroundService(serviceIntent)
////                        }
////
////                    } else
////                        if (work.state.isFinished) {
////                            Log.d(DownloadActivity::javaClass.name, "DONE")
////                            Toast.makeText(
////                                this@DownloadActivity,
////                                getString(R.string.work_done),
////                                Toast.LENGTH_SHORT
////                            ).show()
////                            loaderShow(false)
////                        }
////                }
////            })
//    }
//}