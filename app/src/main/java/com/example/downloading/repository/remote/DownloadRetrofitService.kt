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
//    @GET("/download?file=ODM1NTY4ZDFkYzU1ZTA5ZWEzYTY1ODU0ZDBlNmRmNGFlNTZhZWMyNjJmZDFmYzMxNDNlZmUzMDM0MmRiZGYyZl8xNDRwLm1wNOKYr3kybWV0YS5jb20tMSBzZWMgVklERU_imK8xNDRw")
    @GET("/uc?export=download&id=1IQNB5lNU7i2M_R0chJCk64XlvTh8QmKZ")
    @Streaming
    fun download(): Observable<Response<ResponseBody>>
}

