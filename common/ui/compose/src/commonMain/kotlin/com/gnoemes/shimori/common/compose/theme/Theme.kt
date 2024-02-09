package com.gnoemes.shimori.common.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.gnoemes.shimori.common.compose.LocalShimoriSettings
import com.gnoemes.shimori.settings.AppAccentColor
import com.materialkolor.AnimatedDynamicMaterialTheme
import com.materialkolor.PaletteStyle

@Composable
fun ShimoriTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = false,
    content: @Composable () -> Unit,
) {
    val accentColorType = LocalShimoriSettings
        .current
        .accentColor
        .observe
        .collectAsState(initial = AppAccentColor.Yellow)
        .value

    if (useDynamicColors) {
        MaterialTheme(
            colorScheme = dynamicColorScheme(useDarkColors),
            shapes = ShimoriShapes,
            typography = ShimoriTypography,
            content
        )
    } else {
        val seedColor = secondaryColorFromType(accentColorType)
        AnimatedDynamicMaterialTheme(
            seedColor = seedColor,
            useDarkTheme = useDarkColors,
            style = PaletteStyle.TonalSpot,
            shapes = ShimoriShapes,
            typography = ShimoriTypography,
            content = content
        )
    }
}