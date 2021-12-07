package com.example.downloading

interface DownloadProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)

}