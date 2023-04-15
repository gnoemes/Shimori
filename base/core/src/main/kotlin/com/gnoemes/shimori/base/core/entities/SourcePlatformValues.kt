package com.gnoemes.shimori.base.core.entities

data class SourcePlatformValues(
    val url: String = "",
    val clientId: String = "",
    val secretKey: String = "",
    val oauthRedirect: String = "",
    val userAgent: String = "",
    val signInUrl : String = "",
    val signUpUrl : String = "",
    val oAuthUrl : String = "",
)