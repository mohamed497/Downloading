package com.example.downloading.repository.remote

import com.example.downloading.base.GlobalConstants
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming

interface DownloadRetrofitService {
    @GET(GlobalConstants.RETROFIT_GET_URL)
    @Streaming
    fun download(): Observable<Response<ResponseBody>>
}

