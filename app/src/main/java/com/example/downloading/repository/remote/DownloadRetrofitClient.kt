package com.example.downloading.repository.remote

import com.example.downloading.base.GlobalConstants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .baseUrl(GlobalConstants.BASE_URL)
    .build()

object DownloadRetrofitClient {
    val retrofitService: DownloadRetrofitService by lazy {
        retrofit.create(DownloadRetrofitService::class.java)
    }
}