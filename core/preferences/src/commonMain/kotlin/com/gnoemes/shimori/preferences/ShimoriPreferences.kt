package com.gnoemes.shimori.preferences

import kotlinx.coroutines.flow.Flow

interface ShimoriPreferences {
    var currentCatalogueSource: String?

    var preferredListType: String
    fun observePreferredListType(): Flow<String>

    var preferredListStatus: String
    fun observePreferredListStatus(): Flow<String>

    fun setInt(key: String, value: Int)
    fun getInt(key: String): Int?

    fun setString(key: String, value: String)
    fun getString(key: String): String?


    object ValueKey {
        const val INITIAL_THEME = "initial_theme"
        const val INITIAL_ACCENT = "initial_accent"
        const val INITIAL_LOCALE = "initial_locale"
        const val INITIAL_TITLES_LOCALE = "initial_titles_locale"
    }
}