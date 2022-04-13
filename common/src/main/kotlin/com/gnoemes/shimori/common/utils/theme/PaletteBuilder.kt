package com.gnoemes.shimori.common.utils.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.gnoemes.shimori.common.compose.theme.*

/**
 * Dynamic color-like palette generation but for given primary accent color.
 * @see <a href=https://m3.material.io/styles/color/the-color-system/key-colors-tones>MD3 Color system</a>
 * @return light theme [AppPalette]
 */
fun generateLightPaletteForPrimary(target: Color): AppPalette {
    return createPalette(
        palette = generateAccents(target),
        light = true
    )
}

/**
 * Dynamic color-like palette generation but for given primary accent color.
 * @see <a href=https://m3.material.io/styles/color/the-color-system/key-colors-tones>MD3 Color system</a>
 * @return dark theme [AppPalette]
 */
fun generateDarkPaletteForPrimary(target: Color): AppPalette {
    return createPalette(
        palette = generateAccents(target),
        light = false
    )
}

/**
 * Generates accent and neutral colors
 * @see <a href=https://m3.material.io/styles/color/the-color-system/key-colors-tones>MD3 Color system</a>
 * @return [AccentsPalette]
 */
private fun generateAccents(target: Color): AccentsPalette {
    val a1 = Hct.fromInt(target.toArgb())
    val a2 = a1.createAccent(a1.getHue(), 16f)
    val a3 = a1.createAccent(a1.getHue() + 60, 24f)
    val n1 = a1.createAccent(a1.getHue(), 4f)
    val n2 = a1.createAccent(a1.getHue(), 8f)
    return AccentsPalette(a1, a2, a3, n1, n2)
}

private fun createPalette(
    palette: AccentsPalette,
    light: Boolean
): AppPalette {
    return object : AppPalette {
        override val primary = primary(palette.primary, light)
        override val onPrimary = onPrimary(palette.primary, light)
        override val primaryContainer = primaryContainer(palette.primary, light)
        override val onPrimaryContainer = onPrimaryContainer(palette.primary, light)
        override val inversePrimary = primaryInverse(palette.primary, light)
        override val secondary = secondary(palette.secondary, light)
        override val onSecondary = onSecondary(palette.secondary, light)
        override val secondaryContainer = secondaryContainer(palette.secondary, light)
        override val onSecondaryContainer = onSecondaryContainer(palette.secondary, light)
        override val tertiary = tertiary(palette.tertiary, light)
        override val onTertiary = onTertiary(palette.tertiary, light)
        override val tertiaryContainer = tertiaryContainer(palette.tertiary, light)
        override val onTertiaryContainer = onTertiaryContainer(palette.tertiary, light)
        override val error = if (light) lightError else darkError
        override val onError = if (light) lightOnError else darkOnError
        override val errorContainer = if (light) lightErrorContainer else darkErrorContainer
        override val onErrorContainer = if (light) lightOnErrorContainer else darkOnErrorContainer
        override val background = background(palette.neutral, light)
        override val onBackground = onBackground(palette.neutral, light)
        override val surface = surface(palette.neutral, light)
        override val onSurface = onSurface(palette.neutral, light)
        override val surfaceTint = primary(palette.primary, light).copy(alpha = 0.05f)
        override val inverseSurface = inverseSurface(palette.neutral, light)
        override val inverseOnSurface = inverseOnSurface(palette.neutral, light)
        override val surfaceVariant = surfaceVariant(palette.neutralVariant, light)
        override val onSurfaceVariant = onSurfaceVariant(palette.neutralVariant, light)
        override val outline = outline(palette.neutralVariant, light)
    }
}

private fun primary(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 40f else 80f)
    return hct.color()
}

private fun onPrimary(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 100f else 20f)
    return hct.color()
}

private fun primaryContainer(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 90f else 30f)
    return hct.color()
}

private fun onPrimaryContainer(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 10f else 90f)
    return hct.color()
}

private fun primaryInverse(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 80f else 40f)
    return hct.color()
}

private fun secondary(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 40f else 80f)
    return hct.color()
}

private fun onSecondary(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 100f else 20f)
    return hct.color()
}

private fun secondaryContainer(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 90f else 30f)
    return hct.color()
}

private fun onSecondaryContainer(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 10f else 90f)
    return hct.color()
}

private fun tertiary(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 40f else 80f)
    return hct.color()
}

private fun onTertiary(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 100f else 20f)
    return hct.color()
}

private fun tertiaryContainer(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 90f else 30f)
    return hct.color()
}

private fun onTertiaryContainer(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 10f else 90f)
    return hct.color()
}

private fun background(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 99f else 10f)
    return hct.color()
}

private fun onBackground(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 10f else 90f)
    return hct.color()
}

private fun surface(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 99f else 10f)
    return hct.color()
}

private fun onSurface(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 10f else 90f)
    return hct.color()
}

private fun surfaceVariant(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 90f else 30f)
    return hct.color()
}

private fun onSurfaceVariant(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 30f else 80f)
    return hct.color()
}

private fun outline(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 50f else 60f)
    return hct.color()
}

private fun inverseSurface(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 20f else 90f)
    return hct.color()
}

private fun inverseOnSurface(hct: Hct, light: Boolean): Color {
    hct.setTone(if (light) 95f else 10f)
    return hct.color()
}


private fun Hct.createAccent(newHue: Float, newChroma: Float): Hct {
    return Hct.from(newHue, newChroma, this.getTone())
}

private data class AccentsPalette(
    val primary: Hct,
    val secondary: Hct,
    val tertiary: Hct,
    val neutral: Hct,
    val neutralVariant: Hct,
)

private fun Hct.color() = Color(toInt())