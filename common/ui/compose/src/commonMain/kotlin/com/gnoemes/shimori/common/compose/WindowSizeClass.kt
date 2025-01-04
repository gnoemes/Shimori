package com.gnoemes.shimori.common.compose

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalWindowSizeClass = staticCompositionLocalOf<WindowSizeClass> {
    error("No WindowSizeClass available")
}

@Composable
@ExperimentalMaterial3WindowSizeClassApi
expect fun calculateWindowSizeClass(): WindowSizeClass