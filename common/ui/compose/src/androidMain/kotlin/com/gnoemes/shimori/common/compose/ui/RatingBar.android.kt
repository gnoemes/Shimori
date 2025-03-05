package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize

@Composable
/**
 * Drag version of Rating bar
 */
actual fun RatingBar(
    value: Float,
    initialValue: Float,
    modifier: Modifier,
    contentColor: Color,
    painterFilled: Painter,
    painterHover: Painter,
    painterEmpty: Painter,
    numOfStars: Int,
    size: Dp,
    spaceBetween: Dp,
    isIndicator: Boolean,
    stepSize: StepSize,
    hideInactiveStars: Boolean,
    onValueChange: (Float) -> Unit,
    onRatingChanged: (Float) -> Unit
) {
    var rowSize by remember { mutableStateOf(Size.Zero) }
    var lastDraggedValue by remember { mutableFloatStateOf(0f) }
    val direction = LocalLayoutDirection.current
    val density = LocalDensity.current

    val paddingInPx = remember(spaceBetween) {
        with(density) { spaceBetween.toPx() }
    }
    val starSizeInPx = remember(size) {
        with(density) { size.toPx() }
    }

    Row(modifier = modifier
        .onSizeChanged { rowSize = it.toSize() }
        .pointerInput(
            Unit
        ) {
            detectTapGestures(
                onTap = { position ->
                    if (isIndicator || hideInactiveStars)
                        return@detectTapGestures

                    var calculatedStars =
                        RatingBarUtils.calculateStars(
                            position.x,
                            paddingInPx,
                            numOfStars, stepSize, starSizeInPx
                        )

                    if (direction == LayoutDirection.Rtl)
                        calculatedStars = numOfStars - calculatedStars
                    onRatingChanged(calculatedStars)
                }
            )

            //handling dragging events
            detectHorizontalDragGestures(
                onDragEnd = {
                    if (isIndicator || hideInactiveStars)
                        return@detectHorizontalDragGestures
                    onRatingChanged(lastDraggedValue)
                },
                onDragCancel = {
                },
                onDragStart = {
                },
                onHorizontalDrag = { change, _ ->
                    if (isIndicator || hideInactiveStars)
                        return@detectHorizontalDragGestures

                    change.consume()
                    val dragX = change.position.x.coerceIn(-1f, rowSize.width)
                    var calculatedStars =
                        RatingBarUtils.calculateStars(
                            dragX,
                            paddingInPx,
                            numOfStars, stepSize, starSizeInPx
                        )

                    if (direction == LayoutDirection.Rtl)
                        calculatedStars = numOfStars - calculatedStars
                    onValueChange(calculatedStars)
                    lastDraggedValue = calculatedStars
                }
            )
        }
    ) {
        ComposeStars(
            value,
            false,
            numOfStars,
            size,
            spaceBetween,
            hideInactiveStars,
            contentColor,
            painterEmpty,
            painterHover,
            painterFilled
        )
    }
}