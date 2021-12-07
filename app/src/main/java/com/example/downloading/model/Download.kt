package com.example.downloading.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Download(
    var proggress: Int? = 0,
    var currentFileSize: Int? = 0,
    var totalFileSize: Int? = 0
) : Parcelable {
}