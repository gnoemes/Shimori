package com.gnoemes.shimori.logging.api

interface Logger {
    fun v(throwable: Throwable? = null, tag: String? = null, message: () -> String = { "" }) = Unit

    fun d(throwable: Throwable? = null, tag: String? = null, message: () -> String = { "" }) = Unit

    fun i(throwable: Throwable? = null, tag: String? = null, message: () -> String = { "" }) = Unit

    fun e(throwable: Throwable? = null, tag: String? = null, message: () -> String = { "" }) = Unit

    fun w(throwable: Throwable? = null, tag: String? = null, message: () -> String = { "" }) = Unit
}