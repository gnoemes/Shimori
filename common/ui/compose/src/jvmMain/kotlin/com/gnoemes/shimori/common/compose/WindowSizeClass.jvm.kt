package com.gnoemes.shimori.common.compose

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

@ExperimentalMaterial3WindowSizeClassApi
@Composable
actual fun calculateWindowSizeClass(): WindowSizeClass {
   return androidx.compose.material3.windowsizeclass.calculateWindowSizeClass()
}