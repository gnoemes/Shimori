package com.gnoemes.shimori.shikimori.auth

object ShikimoriConstants {
    const val SIGN_IN_ENDPOINT = "https://shikimori.me/users/sign_in"
    const val SIGN_UP_ENDPOINT = "https://shikimori.me/users/sign_up"
    const val OAUTH_ENDPOINT = "https://shikimori.me/oauth/authorize"
    const val CODE_PATTERN = "(code|error)=(.*?)(?:&|$)"

    const val ERROR_REASON_ACCESS_DENIED = "access_denied"
}