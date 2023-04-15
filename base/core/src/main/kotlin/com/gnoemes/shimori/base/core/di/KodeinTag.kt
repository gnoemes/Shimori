package com.gnoemes.shimori.base.core.di

object KodeinTag {
    const val appName = "appName"
    const val appVersion = "appVersion"
    const val shikimori = "shikimori"
    const val userAgent = "userAgent"
    const val cache = "cache"
    const val imageClient = "imageClient"

    object Shikimori {
        const val tag = "shikimori"
        const val url = "$tag-url"
        const val oAuthRedirect = "$tag-oauth"
        const val clientId = "$tag-client-id"
        const val clientSecret = "$tag-client-secret"
    }
}