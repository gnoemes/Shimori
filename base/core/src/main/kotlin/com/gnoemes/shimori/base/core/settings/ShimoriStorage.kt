package com.gnoemes.shimori.base.core.settings

/**
 * Preference-like storage
 */
interface ShimoriStorage {
    var currentCatalogueSource: String?
    var shikimoriAccessToken: String?
    var shikimoriRefreshToken: String?

    companion object {
        const val SHIKIMORI_ACCESS_TOKEN = "SHIKIMORI_ACCESS_TOKEN"
        const val SHIKIMORI_REFRESH_TOKEN = "SHIKIMORI_REFRESH_TOKEN"
        const val CURRENT_CATALOGUE_SOURCE = "CURRENT_CATALOGUE_SOURCE"
    }
}