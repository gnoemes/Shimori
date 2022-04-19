package com.gnoemes.shimori.base.shared

import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

fun OkHttpClient.Builder.defaultConfig(): OkHttpClient.Builder {
    followRedirects(true)
    connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
    dispatcher(
        Dispatcher().apply { maxRequestsPerHost = 15 }
    )
    return this
}