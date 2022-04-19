package com.gnoemes.shikimori.entities.auth

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)