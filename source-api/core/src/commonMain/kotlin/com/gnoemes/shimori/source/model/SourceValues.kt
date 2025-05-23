package com.gnoemes.shimori.source.model

data class SourceValues(
    val url: String = "",
    val clientId: String = "",
    val secretKey: String = "",
    val oauthRedirect: String = "",
    val signInUrl : String = "",
    val signUpUrl : String = "",
    val oAuthUrl : String = "",
    val tokenUrl : String = "",
    val userAgent : String = ""
)