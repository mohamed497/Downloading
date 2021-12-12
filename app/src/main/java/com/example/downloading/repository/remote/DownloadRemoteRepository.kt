package com.example.downloading.repository.remote

import com.example.downloading.repository.DownloadingRepository
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response

class DownloadRemoteRepository : DownloadingRepository {
    private val downloadService = DownloadRetrofitClient.retrofitService
    override fun download(): Observable<Response<ResponseBody>> {
        return downloadService.download()
    }
}