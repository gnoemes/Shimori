package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.gnoemes.shimori.common.compose.theme.isDynamicColorsAvailable
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.settings.AppAccentColor
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.ShimoriSettings

@Composable
fun ShimoriSettings.shouldUseDarkColors(): Boolean {
    val prefs = LocalShimoriPreferences.current

    val initialTheme = prefs.getString(ShimoriPreferences.ValueKey.INITIAL_THEME)
        ?.let { AppTheme.valueOf(it) }
        ?: AppTheme.SYSTEM
    val themePreference by theme.observe.collectAsState(initialTheme)

    prefs.setString(ShimoriPreferences.ValueKey.INITIAL_THEME, themePreference.name)

    return when (themePreference) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}

@Composable
fun ShimoriSettings.shouldUseDynamicColors(): Boolean {
    return remember { accentColor.observe }
        .collectAsState(AppAccentColor.Yellow)
        .value == AppAccentColor.System && isDynamicColorsAvailable()
}