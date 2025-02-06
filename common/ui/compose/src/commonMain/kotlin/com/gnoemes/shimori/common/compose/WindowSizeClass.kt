package com.gnoemes.shimori.common.compose

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> {
    error("No WindowSizeClass available")
}

@Composable
@ExperimentalMaterial3WindowSizeClassApi
expect fun calculateWindowSizeClass(): WindowSizeClass

fun WindowWidthSizeClass.isCompact() = this == WindowWidthSizeClass.Compact
fun WindowWidthSizeClass.isMedium() = this == WindowWidthSizeClass.Medium
fun WindowWidthSizeClass.isExpanded() = this == WindowWidthSizeClass.Expanded

