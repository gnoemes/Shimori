package com.gnoemes.shimori.common.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ShimoriTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
            colors = if (useDarkColors) ShimoriDarkColors else ShimoriLightColors,
            typography = ShimoriTypography,
            shapes = ShimoriShapes,
            content = content
    )
}