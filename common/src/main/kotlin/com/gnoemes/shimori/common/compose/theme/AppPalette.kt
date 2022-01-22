package com.gnoemes.shimori.common.compose.theme

import androidx.compose.ui.graphics.Color

interface AppPalette {
    val primary : Color
    val onPrimary: Color
    val primaryContainer: Color
    val onPrimaryContainer: Color
    val inversePrimary: Color

    val secondary : Color
    val onSecondary : Color
    val secondaryContainer : Color
    val onSecondaryContainer: Color

    val tertiary: Color
    val onTertiary : Color
    val tertiaryContainer: Color
    val onTertiaryContainer: Color

    val error: Color
    val onError: Color
    val errorContainer: Color
    val onErrorContainer: Color

    val background: Color
    val onBackground: Color
    val surface: Color
    val onSurface: Color
    val inverseSurface: Color
    val inverseOnSurface: Color

    val surfaceVariant: Color
    val onSurfaceVariant: Color
    val outline: Color
}