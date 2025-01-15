package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal expect fun dynamicColorScheme(
    useDarkColors: Boolean
): ColorScheme

expect fun isDynamicColorsAvailable(): Boolean