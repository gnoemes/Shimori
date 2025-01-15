package com.gnoemes.shimori.common.compose.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@SuppressLint("NewApi")
@Composable
internal actual fun dynamicColorScheme(
    useDarkColors: Boolean
): ColorScheme = when {
    useDarkColors -> dynamicDarkColorScheme(LocalContext.current)
    else -> dynamicLightColorScheme(LocalContext.current)
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
actual fun isDynamicColorsAvailable(): Boolean = Build.VERSION.SDK_INT >= 31