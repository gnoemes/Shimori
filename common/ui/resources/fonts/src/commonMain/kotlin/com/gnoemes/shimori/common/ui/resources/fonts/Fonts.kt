package com.gnoemes.shimori.common.ui.resources.fonts

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
expect fun font(
    name: String,
    resourceId: String,
    weight: FontWeight,
    style: FontStyle = FontStyle.Normal
): Font

val ManropeFamily: FontFamily
    @Composable get() = FontFamily(
        font("Manrope", "manrope_400", FontWeight.Normal),
        font("Manrope", "manrope_500", FontWeight.Medium)
    )