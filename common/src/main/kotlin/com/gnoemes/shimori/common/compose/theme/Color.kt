package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val Slate200 = Color(0xFF81A9B3)
private val Slate600 = Color(0xFF4A6572)
private val Slate800 = Color(0xFF232F34)

private val Orange500 = Color(0xFFF9AA33)
private val Orange700 = Color(0xFFC78522)

val shimoriLightColors = lightColors(
        primary = Slate800,
        onPrimary = Color.White,
        primaryVariant = Slate600,
        secondary = Orange700,
        secondaryVariant = Orange500,
        onSecondary = Color.Black,
)

val shimoriDarkColors = darkColors(
        primary = Slate200,
        onPrimary = Color.Black,
        secondary = Orange500,
        onSecondary = Color.Black,
)