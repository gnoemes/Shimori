package com.gnoemes.shimori.common.ui.components

import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.gnoemes.shimori.common.ui.noRippleClickable
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun ButtonRatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    painterNotFilled: Painter,
    painterFilled: Painter,
    colorFilled: Color,
    colorNotFilled: Color,
    starSize: Dp = 32.dp,
    spacing: Dp = 4.dp,
    numStars: Int = 10,
    onRatingChanged: (rating: Float) -> Unit = {}
) {
    var currentRating by remember(rating) { mutableStateOf(rating) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(numStars) { starIndex ->
            val active = starIndex < currentRating.toInt()
            RatingStar(
                size = starSize,
                painter = if (active) painterFilled else painterNotFilled,
                color = if (active) colorFilled else colorNotFilled,
                onClick = {
                    currentRating =
                        //remove rating if same star clicked
                        if (starIndex + 1 == currentRating.toInt()) 0f
                        else starIndex.toFloat() + 1
                    onRatingChanged(currentRating)
                }
            )
        }
    }
}

@Composable
fun RatingStar(
    size: Dp,
    painter: Painter,
    color: Color,
    onClick: () -> Unit,
) {
    val starColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(150),
        label = "Star animation"
    )

    Box(
        modifier = Modifier
            .size(size)
            .noRippleClickable(onClick),
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = starColor
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    color: Color = MaterialTheme.colorScheme.secondary,
    stepSize: StepSize = StepSize.Half,
    numStars: Int = 5,
    onRatingChanged: (rating: Float) -> Unit = {}
) {
    var internalRating = rating
    val rowSize = remember { mutableStateOf(Size.Zero) }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .onSizeChanged { rowSize.value = it.toSize() }
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        internalRating = calculateRating(it.x, rowSize.value.width)
                        onRatingChanged(internalRating.stepRound(stepSize))
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x1 = it.x.coerceIn(0f, rowSize.value.width)
                        internalRating = calculateRating(x1, rowSize.value.width)
                        onRatingChanged(internalRating.stepRound(stepSize))
                    }
                }

                true
            }
    ) {
        (1..numStars).forEach { step ->
            val stepRating = internalRating.stepRound(stepSize)

            val starProgress = when {
                stepRating > step -> 1f
                step.rem(stepRating) < 1 -> stepRating - (step - 1f)
                else -> 0f
            }

            RatingStar(starProgress, color)
        }
    }
}

@Composable
private fun RatingStar(
    rating: Float,
    ratingColor: Color = MaterialTheme.colorScheme.secondary,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(starShape)
    ) {
        Canvas(modifier = Modifier.size(maxHeight)) {
            drawRect(
                brush = SolidColor(backgroundColor),
                size = Size(
                    height = size.height * 1.4f,
                    width = size.width * 1.4f
                ),
                topLeft = Offset(
                    x = -(size.width * 0.1f),
                    y = -(size.height * 0.1f)
                )
            )
            if (rating > 0) {
                drawRect(
                    brush = SolidColor(ratingColor),
                    size = Size(
                        height = size.height * 1.1f,
                        width = size.width * rating
                    )
                )
            }
        }
    }
}

private val starShape = GenericShape { size, _ ->
    addPath(starPath(size.height))
}

private val starPath = { size: Float ->

    Path().apply {
        val outerRadius: Float = size / 3f
        val innerRadius: Double = outerRadius / 2.0
        var rot: Double = Math.PI / 2 * 3
        val cx: Float = size / 2
        val cy: Float = size / 20 * 11
        var x: Float = cx
        var y: Float = cy
        val step = Math.PI / 5


        moveTo(cx, cy - outerRadius)
        repeat(5) {
            x = (cx + cos(rot) * outerRadius).toFloat()
            y = (cy + sin(rot) * outerRadius).toFloat()


            lineTo(x, y)
            rot += step

            x = (cx + cos(rot) * innerRadius).toFloat()
            y = (cy + sin(rot) * innerRadius).toFloat()
            lineTo(x, y)
            rot += step
        }
        close()
    }
}

sealed class StepSize {
    object Raw : StepSize()
    object Half : StepSize()
    object One : StepSize()
}

private fun calculateRating(x: Float, rowWidth: Float, numStars: Int = 5): Float {
    return (x / (rowWidth / numStars)).coerceIn(0f, numStars.toFloat())
}

private fun Float.stepRound(stepSize: StepSize): Float {
    return (when (stepSize) {
        StepSize.Raw -> this
        StepSize.Half -> {
            val part = (this * 10).toInt() % 10
            return when {
                part > 5 -> this.toInt().plus(1f)
                part > 0 -> this.toInt().plus(0.5f)
                else -> this.toInt().toFloat()
            }
        }

        StepSize.One -> this.roundToInt().toFloat()
    })
}