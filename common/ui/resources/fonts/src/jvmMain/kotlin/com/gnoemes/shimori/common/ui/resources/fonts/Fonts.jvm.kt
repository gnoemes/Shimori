package com.gnoemes.shimori.common.ui.resources.fonts

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
actual fun font(
    name: String,
    resourceId: String,
    weight: FontWeight,
    style: FontStyle
): Font {
    return androidx.compose.ui.text.platform.Font("font/$resourceId.ttf", weight, style)
}