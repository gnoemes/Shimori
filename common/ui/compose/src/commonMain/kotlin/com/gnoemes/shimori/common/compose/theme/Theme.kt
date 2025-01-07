package com.gnoemes.shimori.common.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.gnoemes.shimori.common.compose.LocalShimoriPreferences
import com.gnoemes.shimori.common.compose.LocalShimoriSettings
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.settings.AppAccentColor
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle

@Composable
fun ShimoriTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = false,
    content: @Composable () -> Unit,
) {
    val prefs = LocalShimoriPreferences.current

    val accentColorType = LocalShimoriSettings
        .current
        .accentColor
        .observe
        .collectAsState(
            initial =
            (prefs.getInt(ShimoriPreferences.ValueKey.INITIAL_THEME)
                ?: if (useDynamicColors) AppAccentColor.System.value else AppAccentColor.Yellow.value)
                .let { AppAccentColor.from(it) }
        )
        .value

    prefs.setInt(ShimoriPreferences.ValueKey.INITIAL_THEME, accentColorType.value)

    if (useDynamicColors) {
        MaterialTheme(
            colorScheme = dynamicColorScheme(useDarkColors),
            shapes = ShimoriShapes,
            typography = ShimoriTypography,
            content
        )
    } else {
        val seedColor = seedColorFromType(accentColorType)
        DynamicMaterialTheme(
            seedColor = seedColor,
            animate = true,
            useDarkTheme = useDarkColors,
            withAmoled = false,
            style = PaletteStyle.TonalSpot,
            shapes = ShimoriShapes,
            content = content
        )
    }
}

@Composable
fun ShimoriThemePreview(
    useDarkColors: Boolean = true,
    accentColorType: AppAccentColor = AppAccentColor.Yellow,
    content: @Composable () -> Unit,
) {
    val seedColor = seedColorFromType(accentColorType)
    DynamicMaterialTheme(
        seedColor = seedColor,
        animate = true,
        useDarkTheme = useDarkColors,
        withAmoled = false,
        style = PaletteStyle.TonalSpot,
        shapes = ShimoriShapes,
        content = content
    )
}