package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.background
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.gnoemes.shimori.data.common.ShimoriImage
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState
import kotlin.math.pow


@Composable
fun Modifier.gradientBackground(
    painter: Painter?,
    shape: Shape = RectangleShape
): Modifier {
    if (painter == null) return this

    val loader = rememberPainterLoader()
    val dominantColorState = rememberDominantColorState(
        loader = loader
    )

    LaunchedEffect(painter) {
        dominantColorState.updateFrom(painter)
    }

    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to dominantColorState.color.copy(alpha = 0.2f),
            .5f to dominantColorState.color.copy(alpha = 0.0f),
            1f to MaterialTheme.colorScheme.surface,
        ),
        tileMode = TileMode.Clamp
    )

    return this.background(
        brush = brush,
        shape = shape
    )
}

@Composable
fun Modifier.gradientBackground(
    image: ShimoriImage?,
): Modifier {
    if (image == null) {
        return this
    }

    val loader = rememberPainterLoader()
    val context = LocalPlatformContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(image)
            .size(Size.ORIGINAL)
            .build(),
        )
    val state = painter.state
    val dominantColorState = rememberDominantColorState(loader)

    val isSuccess = state.value is AsyncImagePainter.State.Success
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            dominantColorState.updateFrom(painter)
        }
    }

    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to dominantColorState.color.copy(alpha = 0.2f),
            .5f to dominantColorState.color.copy(alpha = 0.0f),
            1f to MaterialTheme.colorScheme.surface,
        ),
        tileMode = TileMode.Clamp
    )

    return this.background(
        brush = brush
    )
}

@Composable
fun Modifier.gradientBackground(
    color: Color = MaterialTheme.colorScheme.secondary,
    containerColor: Color = LocalAbsoluteTonalElevation.current.let {
        MaterialTheme.colorScheme.surfaceColorAtElevation(it)
    }
): Modifier {

    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to color.copy(alpha = 0.2f),
            .5f to color.copy(alpha = 0.0f),
            1f to containerColor,
        ),
        tileMode = TileMode.Clamp
    )

    return this.background(
        brush = brush
    )
}


@Composable
fun Modifier.imageGradientBackground(
    color: Color = MaterialTheme.colorScheme.surface,
): Modifier {
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to color.copy(alpha = 0.16f),
            0.5f to color.copy(alpha = 0.76f),
            1f to color,
        ),
        tileMode = TileMode.Clamp,
    )

    return this.background(
        brush = brush
    )
}


/**
 * Draws a vertical gradient scrim in the foreground.
 *
 * @param color The color of the gradient scrim.
 * @param decay The exponential decay to apply to the gradient. Defaults to `3.0f` which is
 * a cubic decay.
 * @param numStops The number of color stops to draw in the gradient. Higher numbers result in
 * the higher visual quality at the cost of draw performance. Defaults to `16`.
 */
fun Modifier.drawForegroundGradientScrim(
    color: Color,
    decay: Float = 3.0f,
    numStops: Int = 16,
    startY: Float = 0f,
    endY: Float = 1f,
): Modifier = composed {
    val colors = remember(color, numStops) {
        val baseAlpha = color.alpha
        List(numStops) { i ->
            val x = i * 1f / (numStops - 1)
            val opacity = x.pow(decay)
            color.copy(alpha = baseAlpha * opacity)
        }
    }

    drawWithContent {
        drawContent()
        drawRect(
            topLeft = Offset(x = 0f, y = startY * size.height),
            size = size.copy(height = (endY - startY) * size.height),
            brush = Brush.verticalGradient(colors = colors),
        )
    }
}