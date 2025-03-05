package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_star_empty
import com.gnoemes.shimori.common.ui.resources.icons.ic_star_rating_filled
import org.jetbrains.compose.resources.painterResource

sealed interface StepSize {
    data object ONE : StepSize
    data object HALF : StepSize
}

/**
 * @param value is current selected rating count
 * @param numOfStars count of stars to be shown.
 * @param size size for each star
 * @param spaceBetween padding between each star.
 * @param isIndicator isIndicator Whether this rating bar is only an indicator or the value is changeable on user interaction.
 * @param stepSize Can be [StepSize.ONE] or [StepSize.HALF]
 * @param hideInactiveStars Whether the inactive stars should be hidden.
 * @param style the different style applied to the Rating Bar.
 * @param onRatingChanged A function to be called when the click or drag is released and rating value is passed
 */
@Composable
expect fun RatingBar(
    value: Float,
    initialValue: Float = value,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    painterFilled: Painter = painterResource(Icons.ic_star_rating_filled),
    painterHover: Painter = painterResource(Icons.ic_star_rating_filled),
    painterEmpty: Painter = painterResource(Icons.ic_star_empty),
    numOfStars: Int = 5,
    size: Dp = 32.dp,
    spaceBetween: Dp = 6.dp,
    isIndicator: Boolean = false,
    stepSize: StepSize = StepSize.ONE,
    hideInactiveStars: Boolean = false,
    onValueChange: (Float) -> Unit,
    onRatingChanged: (Float) -> Unit
)

@Composable
internal fun ComposeStars(
    value: Float,
    hover: Boolean,
    numOfStars: Int,
    size: Dp,
    spaceBetween: Dp,
    hideInactiveStars: Boolean,
    contentColor: Color,
    painterEmpty: Painter,
    painterHover: Painter,
    painterFilled: Painter
) {
    val ratingPerStar = 1f
    var remainingRating = value

    Row {
        for (i in 1..numOfStars) {
            val starRating = when {
                remainingRating == 0f -> {
                    0f
                }

                remainingRating >= ratingPerStar -> {
                    remainingRating -= ratingPerStar
                    1f
                }

                else -> {
                    val fraction = remainingRating / ratingPerStar
                    remainingRating = 0f
                    fraction
                }
            }
            if (hideInactiveStars && starRating == 0.0f)
                break

            RatingStar(
                fraction = starRating,
                hover = hover,
                modifier = Modifier
                    .padding(
                        start = if (i > 1) spaceBetween / 2 else 0.dp,
                        end = if (i < numOfStars) spaceBetween / 2 else 0.dp
                    )
                    .size(size = size),
                contentColor = contentColor,
                painterEmpty = painterEmpty,
                painterHover = painterHover,
                painterFilled = painterFilled
            )
        }
    }
}

@Composable
fun RatingStar(
    fraction: Float,
    hover: Boolean,
    modifier: Modifier = Modifier,
    contentColor: Color,
    painterEmpty: Painter,
    painterHover: Painter,
    painterFilled: Painter
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    Box(modifier = modifier) {
        if (hover) {
            HoverStar(
                contentColor.copy(alpha = 0.5f),
                fraction,
                isRtl,
                painterHover
            )
        } else {
            FilledStar(
                contentColor,
                fraction,
                isRtl,
                painterFilled
            )
        }

        EmptyStar(contentColor, fraction, isRtl, painterEmpty)
    }
}

@Composable
private fun FilledStar(
    color: Color,
    fraction: Float,
    isRtl: Boolean,
    painterFilled: Painter
) = Canvas(
    modifier = Modifier
        .fillMaxSize()
        .clip(
            if (isRtl) rtlFilledStarFractionalShape(fraction = fraction)
            else FractionalRectangleShape(0f, fraction)
        )
) {
    with(painterFilled) {
        draw(
            size = Size(size.height, size.height),
            colorFilter = ColorFilter.tint(color)
        )
    }
}

@Composable
private fun HoverStar(
    color: Color,
    fraction: Float,
    isRtl: Boolean,
    painterFilled: Painter
) = Canvas(
    modifier = Modifier
        .fillMaxSize()
        .clip(
            if (isRtl) rtlHoverStarFractionalShape(fraction = fraction)
            else FractionalRectangleShape(0f, fraction)
        )
) {
    with(painterFilled) {
        draw(
            size = Size(size.height, size.height),
            alpha = 0.5f,
            colorFilter = ColorFilter.tint(color)
        )
    }
}

@Composable
private fun EmptyStar(
    color: Color,
    fraction: Float,
    isRtl: Boolean,
    painterEmpty: Painter
) =
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                if (isRtl) rtlEmptyStarFractionalShape(fraction = fraction)
                else FractionalRectangleShape(fraction, 1f)
            )
    ) {

        with(painterEmpty) {
            draw(
                size = Size(size.height, size.height),
                colorFilter = ColorFilter.tint(color, BlendMode.SrcAtop)
            )
        }
    }

private fun rtlEmptyStarFractionalShape(fraction: Float): FractionalRectangleShape {
    return if (fraction == 1f || fraction == 0f)
        FractionalRectangleShape(fraction, 1f)
    else FractionalRectangleShape(0f, 1f - fraction)
}

private fun rtlHoverStarFractionalShape(fraction: Float): FractionalRectangleShape {
    return if (fraction == 0f || fraction == 1f)
        FractionalRectangleShape(0f, fraction)
    else FractionalRectangleShape(1f - fraction, 1f)
}

private fun rtlFilledStarFractionalShape(fraction: Float): FractionalRectangleShape {
    return if (fraction == 0f || fraction == 1f)
        FractionalRectangleShape(0f, fraction)
    else FractionalRectangleShape(1f - fraction, 1f)
}


@Stable
private class FractionalRectangleShape(
    private val startFraction: Float,
    private val endFraction: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                left = (startFraction * size.width).coerceAtMost(size.width - 1f),
                top = 0f,
                right = (endFraction * size.width).coerceAtLeast(1f),
                bottom = size.height
            )
        )
    }
}

internal object RatingBarUtils {

    fun calculateStars(
        x: Float,
        horizontalPaddingInPx: Float,
        numOfStars: Int,
        stepSize: StepSize,
        starSizeInPx: Float,
    ): Float {

        if (x <= 0) {
            return 0f
        }

        val starWidthWithRightPadding = starSizeInPx + horizontalPaddingInPx
        val halfStarWidth = starSizeInPx / 2
        for (i in 1..numOfStars) {
            val borderPadding = when {
                i == 1 || i == numOfStars -> horizontalPaddingInPx
                else -> horizontalPaddingInPx / 2
            }
            if (x < (i * starWidthWithRightPadding - borderPadding)) {
                return if (stepSize is StepSize.ONE) {
                    i.toFloat()
                } else {
                    val crossedStarsWidth = (i - 1) * starWidthWithRightPadding
                    val remainingWidth = x - crossedStarsWidth

                    if (remainingWidth <= halfStarWidth) {
                        i.toFloat().minus(0.5f)
                    } else {
                        i.toFloat()
                    }
                }
            }
        }
        return 0f
    }
}