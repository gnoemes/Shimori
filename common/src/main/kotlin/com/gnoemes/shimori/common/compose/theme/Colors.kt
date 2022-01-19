package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln

val ShimoriLightColors = lightColorScheme(
    primary = PaletteLight.primary,
    onPrimary = PaletteLight.onPrimary,
    primaryContainer = PaletteLight.primaryContainer,
    onPrimaryContainer = PaletteLight.onPrimaryContainer,
    inversePrimary = PaletteLight.inversePrimary,
    secondary = PaletteLight.secondary,
    onSecondary = PaletteLight.onSecondary,
    secondaryContainer = PaletteLight.secondaryContainer,
    onSecondaryContainer = PaletteLight.onSecondaryContainer,
    tertiary = PaletteLight.tertiary,
    onTertiary = PaletteLight.onTertiary,
    tertiaryContainer = PaletteLight.tertiaryContainer,
    onTertiaryContainer = PaletteLight.onTertiaryContainer,
    background = PaletteLight.background,
    onBackground = PaletteLight.onBackground,
    surface = PaletteLight.surface,
    onSurface = PaletteLight.onSurface,
    surfaceVariant = PaletteLight.surfaceVariant,
    onSurfaceVariant = PaletteLight.onSurfaceVariant,
    inverseSurface = PaletteLight.inverseSurface,
    inverseOnSurface = PaletteLight.inverseOnSurface,
    error = PaletteLight.error,
    onError = PaletteLight.onError,
    errorContainer = PaletteLight.errorContainer,
    onErrorContainer = PaletteLight.onErrorContainer,
    outline = PaletteLight.outline,
)

val ShimoriDarkColors = darkColorScheme(
    primary = PaletteDark.primary,
    onPrimary = PaletteDark.onPrimary,
    primaryContainer = PaletteDark.primaryContainer,
    onPrimaryContainer = PaletteDark.onPrimaryContainer,
    inversePrimary = PaletteDark.inversePrimary,
    secondary = PaletteDark.secondary,
    onSecondary = PaletteDark.onSecondary,
    secondaryContainer = PaletteDark.secondaryContainer,
    onSecondaryContainer = PaletteDark.onSecondaryContainer,
    tertiary = PaletteDark.tertiary,
    onTertiary = PaletteDark.onTertiary,
    tertiaryContainer = PaletteDark.tertiaryContainer,
    onTertiaryContainer = PaletteDark.onTertiaryContainer,
    background = PaletteDark.background,
    onBackground = PaletteDark.onBackground,
    surface = PaletteDark.surface,
    onSurface = PaletteDark.onSurface,
    surfaceVariant = PaletteDark.surfaceVariant,
    onSurfaceVariant = PaletteDark.onSurfaceVariant,
    inverseSurface = PaletteDark.inverseSurface,
    inverseOnSurface = PaletteDark.inverseOnSurface,
    error = PaletteDark.error,
    onError = PaletteDark.onError,
    errorContainer = PaletteDark.errorContainer,
    onErrorContainer = PaletteDark.onErrorContainer,
    outline = PaletteDark.outline,
)

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
