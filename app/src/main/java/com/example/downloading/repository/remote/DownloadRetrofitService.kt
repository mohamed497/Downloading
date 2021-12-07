package com.example.downloading.repository.remote

import com.example.downloading.base.GlobalConstants
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .baseUrl(GlobalConstants.BASE_URL)
    .build()

interface DownloadRetrofitService {
//    @GET("/download?file=ODM1NTY4ZDFkYzU1ZTA5ZWEzYTY1ODU0ZDBlNmRmNGFlNTZhZWMyNjJmZDFmYzMxNDNlZmUzMDM0MmRiZGYyZl8xNDRwLm1wNOKYr3kybWV0YS5jb20tMSBzZWMgVklERU_imK8xNDRw")
    @GET("files/Node-Android-Chat.zip")
    @Streaming
    fun download(): Observable<Response<ResponseBody>>
}

object DownloadService {
    val retrofitService: DownloadRetrofitService by lazy {
        retrofit.create(DownloadRetrofitService::class.java)
    }
}