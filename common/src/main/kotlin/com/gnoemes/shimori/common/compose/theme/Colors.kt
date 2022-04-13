package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.settings.AppAccentColor
import kotlin.math.ln

fun AppPalette.toColorScheme() : ColorScheme {
    return ColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
    )
}

/**
 * Returns the [ColorScheme.surface] color with an alpha of the [ColorScheme.primary] color overlaid
 * on top of it.
 * Computes the surface tonal color at different elevation levels e.g. surface1 through surface5.
 *
 * @param elevation Elevation value used to compute alpha of the color overlay layer.
 */
fun ColorScheme.surfaceColorAtElevation(
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return surface
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primary.copy(alpha = alpha).compositeOver(surface)
}

/**
 * Returns the [ColorScheme.surfaceVariant] color with an alpha of the [ColorScheme.primary] color overlaid
 * on top of it.
 * Computes the surface tonal color at different elevation levels e.g. surface1 through surface5.
 *
 * @param elevation Elevation value used to compute alpha of the color overlay layer.
 */
fun ColorScheme.surfaceVariantColorAtElevation(
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return surfaceVariant
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primary.copy(alpha = alpha).compositeOver(surfaceVariant)
}

/**
 * Returns the new background [Color] to use, representing the original background [color] with an
 * overlay corresponding to [elevation] applied. The overlay will only be applied to
 * [ColorScheme.surface].
 */
internal fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
    if (backgroundColor == surface) {
        return surfaceColorAtElevation(elevation)
    } else {
        return backgroundColor
    }
}

fun secondaryColorFromType(type: AppAccentColor) = when (type) {
    AppAccentColor.Red -> accentRed
    AppAccentColor.Orange -> accentOrange
    AppAccentColor.Yellow -> accentYellow
    AppAccentColor.Green -> accentGreen
    AppAccentColor.Blue -> accentBlue
    AppAccentColor.Purple -> accentPurple
    else -> accentYellow
}
