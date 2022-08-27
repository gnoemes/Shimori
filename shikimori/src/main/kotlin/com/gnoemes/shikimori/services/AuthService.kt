package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.auth.TokenResponse
import io.ktor.client.request.*

internal interface AuthService {
    suspend fun accessToken(authCode: String): TokenResponse?
    suspend fun refreshToken(
        refreshToken: String,
        block: HttpRequestBuilder.() -> Unit
    ): TokenResponse?
}
