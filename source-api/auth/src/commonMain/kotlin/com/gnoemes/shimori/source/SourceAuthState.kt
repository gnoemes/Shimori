package com.gnoemes.shimori.source

interface SourceAuthState {
    val sourceId: Long
    val accessToken: String
    val refreshToken: String
    val isAuthorized: Boolean

    fun serializeToJson(): String
}

