package com.gnoemes.shimori.common.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.gnoemes.shimori.base.settings.AppTheme
import com.gnoemes.shimori.base.settings.ShimoriSettings

@Composable
fun ShimoriSettings.shouldUseDarkColors() : Boolean {
    val themePreference = theme.observe.collectAsStateWithLifecycle(initial = AppTheme.SYSTEM)
    return when (themePreference.value) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}