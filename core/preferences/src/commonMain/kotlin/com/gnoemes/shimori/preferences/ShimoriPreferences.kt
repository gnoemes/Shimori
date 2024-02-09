package com.gnoemes.shimori.preferences

import kotlinx.coroutines.flow.Flow

interface ShimoriPreferences {
    var currentCatalogueSource: String?

    var preferredListType: Int
    fun observePreferredListType() : Flow<Int>

    var preferredListStatus: String
    fun observePreferredListStatus() : Flow<String>
}