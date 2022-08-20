package com.gnoemes.shimori.common.ui.utils

import android.content.Context
import androidx.collection.LruCache
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberDominantColorState(
    context: Context = LocalContext.current,
    defaultColor: Color = MaterialTheme.colors.primary,
    defaultOnColor: Color = MaterialTheme.colors.onPrimary,
    cacheSize: Int = 12,
    isColorValid: (Color) -> Boolean = { true }
): DominantColorState = remember {
    DominantColorState(context, defaultColor, defaultOnColor, cacheSize, isColorValid)
}


/**
 * A class which stores and caches the result of any calculated dominant colors
 * from images.
 *
 * @param context Android context
 * @param defaultColor The default color, which will be used if [calculateDominantColor] fails to
 * calculate a dominant color
 * @param defaultOnColor The default foreground 'on color' for [defaultColor].
 * @param cacheSize The size of the [LruCache] used to store recent results. Pass `0` to
 * disable the cache.
 * @param isColorValid A lambda which allows filtering of the calculated image colors.
 */
@Stable
class DominantColorState(
    private val context: Context,
    private val defaultColor: Color,
    private val defaultOnColor: Color,
    cacheSize: Int = 12,
    private val isColorValid: (Color) -> Boolean = { true }
) {
    var dominant by mutableStateOf(defaultColor)
        private set
    var onDominant by mutableStateOf(defaultOnColor)
        private set

    var middle by mutableStateOf(defaultColor)
        private set
    var onMiddle by mutableStateOf(defaultOnColor)
        private set

    private val cache = when {
        cacheSize > 0 -> LruCache<String, DominantColors>(cacheSize)
        else -> null
    }

    suspend fun updateColorsFromImageUrl(url: String) {
        val result = calculateDominantColor(url)
        dominant = result?.dominant ?: defaultColor
        onDominant = result?.onDominant ?: defaultOnColor
        middle = result?.middle ?: defaultColor
        onMiddle = result?.onMiddle ?: defaultOnColor
    }

    private suspend fun calculateDominantColor(url: String): DominantColors? {
        val cached = cache?.get(url)
        if (cached != null) {
            // If we already have the result cached, return early now...
            return cached
        }

        val swatches = calculateSwatchesInImage(context, url).sortedByDescending { it.population }

        val middle = swatches
            .drop(swatches.size / 2)
            .firstOrNull { swatch -> isColorValid(Color(swatch.rgb)) }

        val dominant = swatches
            //take second
            .drop(1)
            .firstOrNull { swatch -> isColorValid(Color(swatch.rgb)) }

        return if (middle != null && dominant != null) {
            DominantColors(
                dominant = Color(dominant.rgb),
                onDominant = Color(dominant.bodyTextColor).copy(alpha = 1f),
                middle = Color(middle.rgb),
                onMiddle = Color(middle.bodyTextColor).copy(alpha = 1f),
            ).also { result -> cache?.put(url, result) }
        } else null
    }

    /**
     * Reset the color values to [defaultColor].
     */
    fun reset() {
        dominant = defaultColor
        onDominant = defaultColor
        middle = defaultColor
        onMiddle = defaultOnColor
    }
}

@Immutable
private data class DominantColors(
    val dominant: Color,
    val onDominant: Color,
    val middle: Color,
    val onMiddle: Color,
)


/**
 * Fetches the given [imageUrl] with Coil, then uses [Palette] to calculate the dominant color.
 */
private suspend fun calculateSwatchesInImage(
    context: Context,
    imageUrl: String
): List<Palette.Swatch> {
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        // We scale the image to cover 128px x 128px (i.e. min dimension == 128px)
        .size(128).scale(Scale.FILL)
        // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
        .allowHardware(false)
        // Set a custom memory cache key to avoid overwriting the displayed image in the cache
        .memoryCacheKey("$imageUrl.palette")
        .build()

    val bitmap = when (val result = context.imageLoader.execute(request)) {
        is SuccessResult -> result.drawable.toBitmap()
        else -> null
    }

    return bitmap?.let {
        withContext(Dispatchers.Default) {
            val palette = Palette.Builder(bitmap)
                // Disable any bitmap resizing in Palette. We've already loaded an appropriately
                // sized bitmap through Coil
                .resizeBitmapArea(0)
                // Clear any built-in filters. We want the unfiltered dominant color
                .clearFilters()
                // We reduce the maximum color count down to 8
                .maximumColorCount(8)
                .generate()

            palette.swatches
        }
    } ?: emptyList()
}