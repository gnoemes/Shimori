package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.common.TokenResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/oauth/token")
    fun refreshToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("refresh_token") refreshToken: String,
        @Query("grant_type") grantType : String = "refresh_token"
    ): Response<TokenResponse>
}