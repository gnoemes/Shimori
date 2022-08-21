package com.gnoemes.shimori.base.core.settings

import kotlinx.coroutines.flow.Flow

interface ShimoriSettings {
    val titlesLocale: Setting<AppTitlesLocale>
    val locale: Setting<AppLocale>
    val showSpoilers: Setting<Boolean>
    val theme: Setting<AppTheme>
    val accentColor: Setting<AppAccentColor>

    val preferredListType: Setting<Int>
    val preferredListStatus: Setting<String>
}

interface Setting<T> {
    suspend fun update(newState: T)
    val observe: Flow<T>
}

suspend operator fun <T> Setting<T>.invoke(newState: T) = update(newState = newState)