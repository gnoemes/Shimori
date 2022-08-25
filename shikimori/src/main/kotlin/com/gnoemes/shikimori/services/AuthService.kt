package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.auth.TokenResponse
import io.ktor.client.*

internal interface AuthService {
    suspend fun accessToken(authCode : String) : TokenResponse?
    suspend fun HttpClient.refreshToken(refreshToken: String): TokenResponse?
}
