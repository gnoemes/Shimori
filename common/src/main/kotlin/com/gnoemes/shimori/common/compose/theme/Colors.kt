package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors

val ShimoriLightColors = lightColors(
        primary = lightPrimary,
        primaryVariant = lightPrimary,
        secondary = secondary,
        secondaryVariant = secondaryAlpha,
        background = lightPrimary,
        surface = lightSurface,
        onPrimary = lightOnPrimary,
        onSecondary = onSecondary,
        onBackground = lightOnPrimary,
        onSurface = lightOnSurface
)

val ShimoriDarkColors = darkColors(
        primary = darkPrimary,
        primaryVariant = darkPrimary,
        secondary = secondary,
        secondaryVariant = secondaryAlpha,
        background = darkPrimary,
        surface = darkSurface,
        onPrimary = darkOnPrimary,
        onSecondary = onSecondary,
        onBackground = darkOnPrimary,
        onSurface = darkOnSurface
)


val Colors.toolbar get() = if (isLight) lightToolbar else darkToolbar
val Colors.tabsSurface get() = if (isLight) lightTabsSurface else darkTabsSurface
val Colors.button get() = if (isLight) lightButton else darkButton
val Colors.caption get() = if (isLight) lightCaption else darkCaption
val Colors.disabled get() = if (isLight) lightDisabled else darkDisabled
val Colors.alpha get() = if (isLight) lightAlpha else darkAlpha