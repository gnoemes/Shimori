package com.gnoemes.shimori.common.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.gnoemes.shimori.common.compose.LocalShimoriSettings

@Composable
fun ShimoriTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val dynamicColorAvailable = false
//    val dynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val dynamicColorSettingEnabled = LocalShimoriSettings
        .current
        .dynamicColors
        .observe
        .collectAsState(initial = dynamicColorAvailable)

    val dynamicColor = dynamicColorAvailable && dynamicColorSettingEnabled.value

    val colorScheme = when {
        dynamicColor && useDarkColors -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !useDarkColors -> dynamicLightColorScheme(LocalContext.current)
        useDarkColors -> ShimoriDarkColors
        else -> ShimoriLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShimoriTypography,
        content = content
    )
}