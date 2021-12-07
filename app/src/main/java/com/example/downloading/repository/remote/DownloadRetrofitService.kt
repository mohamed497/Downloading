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
//    @GET("/uc?export=download&id=1IQNB5lNU7i2M_R0chJCk64XlvTh8QmKZ")
    @GET("/uploads/2017/04/file_example_MP4_640_3MG.mp4")
    @Streaming
    fun download(): Observable<Response<ResponseBody>>
}

