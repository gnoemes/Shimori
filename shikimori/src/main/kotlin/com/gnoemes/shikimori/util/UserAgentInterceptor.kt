package com.gnoemes.shikimori.util

import com.gnoemes.shikimori.Shikimori
import okhttp3.Interceptor
import okhttp3.Response

internal class UserAgentInterceptor : Interceptor {

    companion object {
        const val USER_AGENT_HEADER = "User-agent"
        const val USER_AGENT_CLIENT = "Shimori"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (Shikimori.BASE_URL != request.url.host) {
            return chain.proceed(request)
        }

        val builder = request.newBuilder()
        builder.addHeader(USER_AGENT_HEADER, USER_AGENT_CLIENT)
        return chain.proceed(builder.build())
    }
}