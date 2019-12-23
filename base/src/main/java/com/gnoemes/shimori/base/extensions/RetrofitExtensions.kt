package com.gnoemes.shimori.base.extensions

import com.gnoemes.shimori.base.entities.ErrorResult
import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.base.entities.Success
import retrofit2.HttpException
import retrofit2.Response

@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
suspend fun <T> Response<T>.toResult(): Result<T> = toResult { it }

suspend fun <T, E> Response<T>.toResult(mapper: suspend (T) -> E): Result<E> {
    return try {
        if (isSuccessful) {
            Success(data = mapper(bodyOrThrow()), responseModified = isFromNetwork())
        } else {
            ErrorResult(toException())
        }
    } catch (e: Exception) {
        ErrorResult(e)
    }
}

fun <T> Response<T>.toException() = HttpException(this)

fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) throw HttpException(this)
    return body()!!
}

fun <T> Response<T>.isFromNetwork(): Boolean {
    return raw().cacheResponse == null
}