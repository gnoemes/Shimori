package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.auth.TokenResponse

internal interface AuthService {
    suspend fun accessToken(authCode : String) : TokenResponse?
    suspend fun refreshToken(refreshToken: String): TokenResponse?
}
