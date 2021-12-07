package com.example.downloading.repository.remote

import com.example.downloading.repository.DownloadingRepository
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response

class DownloadRemoteRepository : DownloadingRepository {
    private val downloadService = DownloadService.retrofitService
    override fun download(): Observable<Response<ResponseBody>> {
        return downloadService.download()
    }
}