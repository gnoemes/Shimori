package com.gnoemes.shimori.common.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.gnoemes.shimori.base.settings.ShimoriPreferences

@Composable
fun ShimoriPreferences.shouldUseDarkColors(): Boolean {
    val themePreference = observeTheme().collectAsState(initial = ShimoriPreferences.Theme.SYSTEM)
    return when (themePreference.value) {
        ShimoriPreferences.Theme.LIGHT -> false
        ShimoriPreferences.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}