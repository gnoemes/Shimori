package com.gnoemes.shimori.settings

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.base.settings.AppAccentColor
import com.gnoemes.shimori.base.settings.AppLocale
import com.gnoemes.shimori.base.settings.AppTheme
import com.gnoemes.shimori.base.settings.AppTitlesLocale

@Immutable
data class SettingsViewState(
    val appVersion : String,
    val titlesLocale : AppTitlesLocale,
    val locale : AppLocale,
    val showSpoilers : Boolean,
    val theme : AppTheme,
    val accentColor : AppAccentColor
)