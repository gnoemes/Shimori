package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.auth.TokenResponse
import retrofit2.Call
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
    ): Call<TokenResponse>
}