package com.example.downloading.repository

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response

interface DownloadingRepository {

    fun download(): Observable<Response<ResponseBody>>
}