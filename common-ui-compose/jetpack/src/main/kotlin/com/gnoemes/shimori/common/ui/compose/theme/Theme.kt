package com.gnoemes.shimori.common.ui.compose.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import com.gnoemes.shimori.base.core.settings.AppAccentColor
import com.gnoemes.shimori.common.ui.compose.utils.generateDarkPaletteForPrimary
import com.gnoemes.shimori.common.ui.compose.utils.generateLightPaletteForPrimary
import com.gnoemes.shimori.common.ui.compose.LocalShimoriSettings

@Composable
fun ShimoriTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val prefs = LocalContext.current.getSharedPreferences("defaults", Context.MODE_PRIVATE)

    val dynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val accentColorType = LocalShimoriSettings
        .current
        .accentColor
        .observe
        .collectAsState(
            initial = prefs.getInt(
                "initial_theme",
                if (dynamicColorAvailable) AppAccentColor.System.value
                else AppAccentColor.Yellow.value
            ).let { AppAccentColor.from(it) }
        )
        .value

    prefs.edit { putInt("initial_theme", accentColorType.value) }

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

@Composable
fun ShimoriThemePreview(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    accentColorType : AppAccentColor = AppAccentColor.System,
    content: @Composable () -> Unit
) {
    val accentColor = secondaryColorFromType(accentColorType)

    val colorScheme = when {
        useDarkColors -> generateDarkPaletteForPrimary(accentColor).toColorScheme()
        else -> generateLightPaletteForPrimary(accentColor).toColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShimoriTypography,
        content = content
    )
}