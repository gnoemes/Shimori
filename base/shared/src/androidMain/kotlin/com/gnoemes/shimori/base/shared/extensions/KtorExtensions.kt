package com.gnoemes.shimori.base.shared.extensions

import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

fun OkHttpClient.Builder.defaultConfig(
    maxIdleConnections : Int = 10,
    maxRequestPerHost: Int = 15,
    keepAliveDuration : Long = 2000
): OkHttpClient.Builder {
    followRedirects(true)
    connectionPool(ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS))
    dispatcher(
        Dispatcher().apply { maxRequestsPerHost = maxRequestPerHost }
    )
    return this
}