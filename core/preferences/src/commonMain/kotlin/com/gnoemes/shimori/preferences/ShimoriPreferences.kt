package com.gnoemes.shimori.preferences

import kotlinx.coroutines.flow.Flow

interface ShimoriPreferences {
    var currentCatalogueSource: String?

    //TODO delete
    var shikimoriAccessToken: String?
    var shikimoriRefreshToken: String?

    var preferredListType: Int
    fun observePreferredListType() : Flow<Int>

    var preferredListStatus: String
    fun observePreferredListStatus() : Flow<String>
}