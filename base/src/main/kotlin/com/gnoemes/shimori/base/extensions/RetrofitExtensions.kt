package com.gnoemes.shimori.base.extensions

import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

inline fun <T> Response<T>.bodyOrThrow(): T = if (isSuccessful) body()!! else throw HttpException(this)

suspend fun <T> withRetry(
    defaultDelay: Long = 300,
    maxAttempts: Int = 3,
    shouldRetry: (Throwable) -> Boolean = ::defaultShouldRetry,
    block: suspend () -> T
): T {
    repeat(maxAttempts) { attempt ->
        val response = runCatching { block() }

        when {
            response.isSuccess -> return response.getOrThrow()
            response.isFailure -> {
                val exception = response.exceptionOrNull()!!

                // The response failed, so lets see if we should retry again
                if (attempt == maxAttempts - 1 || !shouldRetry(exception)) {
                    throw exception
                }

                var nextDelay = attempt * attempt * defaultDelay

                if (exception is HttpException) {
                    // If we have a HttpException, check whether we have a Retry-After
                    // header to decide how long to delay
                    exception.retryAfter?.let {
                        nextDelay = it.coerceAtLeast(defaultDelay)
                    }
                }

                delay(nextDelay)
            }
        }
    }

    // We should never hit here
    throw IllegalStateException("Unknown exception from executeWithRetry")
}

private val HttpException.retryAfter: Long?
    get() {
        val retryAfterHeader = response()?.headers()?.get("Retry-After")
        if (retryAfterHeader != null && retryAfterHeader.isNotEmpty()) {
            // Got a Retry-After value, try and parse it to an long
            try {
                return retryAfterHeader.toLong() + 10
            } catch (nfe: NumberFormatException) {
                // Probably won't happen, ignore the value and use the generated default above
            }
        }
        return null
    }

private fun defaultShouldRetry(throwable: Throwable) = when (throwable) {
    is HttpException -> throwable.code() == 429
    is IOException -> true
    else -> false
}
