package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun dynamicColorScheme(
    useDarkColors: Boolean
): ColorScheme = throw IllegalStateException("Dynamic color scheme is not supported for JVM")

internal actual fun isDynamicColorsAvailable(): Boolean = false