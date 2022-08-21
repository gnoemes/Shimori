package com.gnoemes.shimori.settings

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.base.core.settings.AppAccentColor
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTheme
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale

@Immutable
data class SettingsViewState(
    val appVersion: String,
    val titlesLocale: AppTitlesLocale,
    val locale: AppLocale,
    val showSpoilers: Boolean,
    val theme: AppTheme,
    val accentColor: AppAccentColor
)