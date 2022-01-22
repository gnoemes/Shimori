package com.gnoemes.shimori.common.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.gnoemes.shimori.base.settings.AppAccentColor
import com.gnoemes.shimori.common.compose.LocalShimoriSettings
import com.gnoemes.shimori.common.utils.theme.generateDarkPaletteForPrimary
import com.gnoemes.shimori.common.utils.theme.generateLightPaletteForPrimary

@Composable
fun ShimoriTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val dynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val accentColorType = LocalShimoriSettings
        .current
        .accentColor
        .observe
        .collectAsState(
            initial = if (dynamicColorAvailable) AppAccentColor.System
            else AppAccentColor.Yellow
        )
        .value

    val dynamicColor = dynamicColorAvailable
            && accentColorType == AppAccentColor.System

    val accentColor = secondaryColorFromType(accentColorType)

    val colorScheme = when {
        dynamicColor && useDarkColors -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !useDarkColors -> dynamicLightColorScheme(LocalContext.current)
        useDarkColors -> generateDarkPaletteForPrimary(accentColor).toColorScheme()
        else -> generateLightPaletteForPrimary(accentColor).toColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShimoriTypography,
        content = content
    )
}