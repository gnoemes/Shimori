package com.gnoemes.shimori.base.core.settings

/**
 * Preference-like storage
 */
interface ShimoriStorage {
    var shikimoriAccessToken: String?
    var shikimoriRefreshToken: String?

    companion object {
        const val SHIKIMORI_ACCESS_TOKEN = "SHIKIMORI_ACCESS_TOKEN"
        const val SHIKIMORI_REFRESH_TOKEN = "SHIKIMORI_REFRESH_TOKEN"
    }
}