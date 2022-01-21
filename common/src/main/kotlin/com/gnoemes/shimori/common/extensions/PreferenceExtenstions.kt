package com.gnoemes.shimori.common.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.gnoemes.shimori.base.settings.ShimoriSettings

@Composable
fun ShimoriSettings.shouldUseDarkColors() : Boolean {
    val themePreference = theme.observe.collectAsState(initial = ShimoriSettings.Theme.SYSTEM)
    return when (themePreference.value) {
        ShimoriSettings.Theme.LIGHT -> false
        ShimoriSettings.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}