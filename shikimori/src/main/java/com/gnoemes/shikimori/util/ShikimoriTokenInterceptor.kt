package com.gnoemes.shikimori.util

import com.gnoemes.shikimori.Shikimori
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ShikimoriTokenInterceptor @Inject constructor(
    private val shikimori: Shikimori
) : Interceptor {

    companion object {
        private const val ACCESS_TOKEN_HEADER = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request().newBuilder()

        val accessToken = shikimori.accessToken
        if (!accessToken.isNullOrBlank()) {
            builder.header(ACCESS_TOKEN_HEADER, "Bearer %s".format(accessToken))
        }

        return chain.proceed(builder.build())
    }
}