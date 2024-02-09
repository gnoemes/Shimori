package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.gnoemes.shimori.common.compose.theme.isDynamicColorsAvailable
import com.gnoemes.shimori.settings.AppAccentColor
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.ShimoriSettings

@Composable
fun ShimoriSettings.shouldUseDarkColors(): Boolean {
    val themePreference by theme.observe.collectAsState(initial = AppTheme.SYSTEM)

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