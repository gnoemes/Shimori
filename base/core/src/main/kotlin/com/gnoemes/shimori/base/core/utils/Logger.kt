package com.gnoemes.shimori.base.core.utils

interface Logger {
    fun v(message: String, tag: String? = null, t: Throwable? = null)
    fun v(t: Throwable)
    fun i(message: String, tag: String? = null, t: Throwable? = null)
    fun i(t: Throwable)
    fun e(message: String, tag: String? = null, t: Throwable? = null)
    fun e(t: Throwable)
}