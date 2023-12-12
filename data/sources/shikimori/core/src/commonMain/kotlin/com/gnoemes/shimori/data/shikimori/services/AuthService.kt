package com.gnoemes.shimori.data.shikimori.services

import com.gnoemes.shimori.data.shikimori.models.auth.TokenResponse
import io.ktor.client.request.HttpRequestBuilder

internal interface AuthService {
    suspend fun accessToken(authCode: String): TokenResponse?
    suspend fun refreshToken(
        refreshToken: String,
        block: HttpRequestBuilder.() -> Unit
    ): TokenResponse?
}
