package com.gnoemes.shimori.common.compose

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
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

fun WindowHeightSizeClass.isCompact() = this == WindowHeightSizeClass.Compact
fun WindowHeightSizeClass.isMedium() = this == WindowHeightSizeClass.Medium
fun WindowHeightSizeClass.isExpanded() = this == WindowHeightSizeClass.Expanded

fun WindowSizeClass.isCompact() = widthSizeClass.isCompact() && !heightSizeClass.isExpanded()
fun WindowSizeClass.isMedium() = !isExpanded() && !isCompact()
fun WindowSizeClass.isExpanded() = widthSizeClass.isExpanded() && heightSizeClass.isExpanded()

