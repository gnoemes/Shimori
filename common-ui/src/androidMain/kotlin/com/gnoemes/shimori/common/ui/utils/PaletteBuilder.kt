package com.gnoemes.shimori.common.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.gnoemes.shimori.common.ui.theme.*
import kotlin.math.max

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
 * @return [CorePalette]
 */
private fun generateAccents(target: Color): CorePalette {
    return CorePalette.from(target.toArgb())
}

private fun createPalette(
    palette: CorePalette,
    light: Boolean
): AppPalette {
    return object : AppPalette {
        override val primary = palette.primary.tone(if (light) 40 else 80).color()
        override val onPrimary = palette.primary.tone(if (light) 100 else 20).color()
        override val primaryContainer = palette.primary.tone(if (light) 90 else 30).color()
        override val onPrimaryContainer = palette.primary.tone(if (light) 10 else 90).color()
        override val inversePrimary = palette.primary.tone(if (light) 80 else 40).color()
        override val secondary = palette.secondary.tone(if (light) 40 else 80).color()
        override val onSecondary = palette.secondary.tone(if (light) 100 else 20).color()
        override val secondaryContainer = palette.secondary.tone(if (light) 90 else 30).color()
        override val onSecondaryContainer = palette.secondary.tone(if (light) 10 else 90).color()
        override val tertiary = palette.tertiary.tone(if (light) 40 else 80).color()
        override val onTertiary = palette.tertiary.tone(if (light) 100 else 20).color()
        override val tertiaryContainer = palette.tertiary.tone(if (light) 90 else 30).color()
        override val onTertiaryContainer = palette.tertiary.tone(if (light) 10 else 90).color()
        override val error = if (light) lightError else darkError
        override val onError = if (light) lightOnError else darkOnError
        override val errorContainer = if (light) lightErrorContainer else darkErrorContainer
        override val onErrorContainer = if (light) lightOnErrorContainer else darkOnErrorContainer
        override val background = palette.neutral.tone(if (light) 99 else 10).color()
        override val onBackground = palette.neutral.tone(if (light) 10 else 90).color()
        override val surface = palette.neutral.tone(if (light) 99 else 10).color()
        override val onSurface = palette.neutral.tone(if (light) 10 else 90).color()
        override val surfaceTint = primary.copy(alpha = 0.05f)
        override val inverseSurface = palette.neutral.tone(if (light) 20 else 90).color()
        override val inverseOnSurface = palette.neutral.tone(if (light) 95 else 10).color()
        override val surfaceVariant = palette.neutralVariant.tone(if (light) 90 else 30).color()
        override val onSurfaceVariant = palette.neutralVariant.tone(if (light) 30 else 80).color()
        override val outline = palette.neutralVariant.tone(if (light) 50 else 60).color()
        override val outlineVariant = palette.neutralVariant.tone(if (light) 80 else 30).color()
        override val scrim = palette.neutral.tone(0).color().copy(alpha = .32f)
    }
}

private class CorePalette(
    val primary: TonalPalette,
    val secondary: TonalPalette,
    val tertiary: TonalPalette,
    val neutral: TonalPalette,
    val neutralVariant: TonalPalette,
) {

    companion object {
        internal fun from(argb: Int): CorePalette {
            val hct = Hct.fromInt(argb)
            val hue = hct.getHue()
            val chroma = hct.getChroma()

            return CorePalette(
                TonalPalette(hue, max(40f, chroma)),
                TonalPalette(hue, 16f),
                TonalPalette(hue + 60f, 24f),
                TonalPalette(hue, 4f),
                TonalPalette(hue, 8f)
            )
        }
    }
}

private data class TonalPalette(
    val hue: Float,
    val chroma: Float
) {
    fun tone(tone: Int): Int {
        return Hct.from(this.hue, this.chroma, tone.toFloat()).toInt()
    }
}

private fun Int.color() = Color(this)