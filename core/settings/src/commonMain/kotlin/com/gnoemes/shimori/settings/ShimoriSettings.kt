package com.gnoemes.shimori.settings

import kotlinx.coroutines.flow.Flow

interface ShimoriSettings {
    val titlesLocale: Setting<AppTitlesLocale>
    val locale: Setting<AppLocale>
    val showSpoilers: Setting<Boolean>
    val theme: Setting<AppTheme>
    val accentColor: Setting<AppAccentColor>
    val onProgressCompleteAction: Setting<OnProgressCompleteAction>
    val onProgressNotInWatchingAction: Setting<OnProgressNotInWatchingAction>
}

interface Setting<T> {
    suspend fun update(newState: T)
    suspend fun get(): T
    val observe: Flow<T>
}

suspend operator fun <T> Setting<T>.invoke(newState: T) = update(newState = newState)