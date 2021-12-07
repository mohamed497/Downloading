package com.example.downloading.repository

import com.example.downloading.repository.remote.DownloadRemoteRepository
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response

class DownloadingRepositoryImpl : DownloadingRepository {

    private val remoteRepo: DownloadingRepository = DownloadRemoteRepository()
    override fun download(): Observable<Response<ResponseBody>> {
        return remoteRepo.download()
    }
}
