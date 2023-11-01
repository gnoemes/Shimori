package com.gnoemes.shimori.main

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale

@Immutable
data class AppSettingsState(
    val titlesLocale: AppTitlesLocale,
    val appLocale: AppLocale,
)