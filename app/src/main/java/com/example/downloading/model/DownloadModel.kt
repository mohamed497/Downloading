package com.example.downloading.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
//data class Download(
//    var proggress: Int? = 0,
//    var currentFileSize: Int? = 0,
//    var totalFileSize: Int? = 0
//) : Parcelable


@Parcelize
data class DownloadModel(
    var message: String,
    var proggress: Int,
    var currentFileSize: Int,
    var totalFileSize: Int
):Parcelable{
//    companion object{
//        private var instance : DownloadModel? = null
//        fun  getInstance(progress: Int? = 0, currentFileSize: Int? = 0, totalFileSize: Int? = 0): DownloadModel {
//            if (instance == null)
//                instance = DownloadModel(progress,currentFileSize,totalFileSize)
//
//            return instance!!
//        }
//
//    }
}

