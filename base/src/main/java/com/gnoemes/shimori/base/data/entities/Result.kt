package com.gnoemes.shimori.base.data.entities


sealed class Result<T> {
    open fun get(): T? = null
}

data class Success<T>(val data: T, val responseModified: Boolean = true) : Result<T>() {
    override fun get(): T = data
}

data class ErrorResult<T>(
    val throwable: Throwable? = null,
    val message: String? = null
) : Result<T>()