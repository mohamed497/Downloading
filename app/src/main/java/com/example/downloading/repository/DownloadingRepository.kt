package com.example.downloading.repository

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response

interface DownloadingRepository {

    fun download(): Observable<Response<ResponseBody>>
}