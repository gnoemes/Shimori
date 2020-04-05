package com.gnoemes.shikimori

import com.gnoemes.shikimori.entities.auth.TokenResponse
import com.gnoemes.shikimori.services.AuthService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Shikimori @Inject constructor(
    @Named("shikimori-client-id") private val clientId: String,
    @Named("shikimori-secret-key") private val clientSecret: String,
    @Named("shikimori-oauth-redirect") private val redirect: String,
    private val authService: AuthService
) {

    companion object {
        const val API_BASE_URL = "https://shikimori.one/api/"
        const val BASE_HOST = "shikimori.one"
        const val BASE_URL = "https://$BASE_HOST/"
    }

    var accessToken: String? = null
    var refreshToken: String? = null

    fun refreshToken(refreshToken: String): Response<TokenResponse> {
        return authService.refreshToken(
                clientId,
                clientSecret,
                redirect,
                refreshToken
        ).execute()
    }

}