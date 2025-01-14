package com.gnoemes.shimori.preferences

import kotlinx.coroutines.flow.Flow

interface ShimoriPreferences {
    var currentCatalogueSource: String?

    var preferredListType: Int
    fun observePreferredListType(): Flow<Int>

    var preferredListStatus: String
    fun observePreferredListStatus(): Flow<String>

    fun setInt(key: String, value: Int)
    fun getInt(key: String): Int?


    object ValueKey {
        const val INITIAL_THEME = "initial_theme"
    }
}