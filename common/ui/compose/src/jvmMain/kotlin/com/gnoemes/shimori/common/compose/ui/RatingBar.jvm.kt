package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize

/**
 * Hover version of Rating bar
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
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
    val direction = LocalLayoutDirection.current
    val density = LocalDensity.current
    var hover by remember { mutableStateOf(false) }

    val paddingInPx = remember(spaceBetween) {
        with(density) { spaceBetween.toPx() }
    }
    val starSizeInPx = remember(size) {
        with(density) { size.toPx() }
    }

    Row(modifier = modifier
        .onSizeChanged { rowSize = it.toSize() }
        .onPointerEvent(PointerEventType.Enter) { event ->
            if (isIndicator || hideInactiveStars)
                return@onPointerEvent

            hover = true
            val change = event.changes.last()

            var calculatedStars = RatingBarUtils.calculateStars(
                change.position.x,
                paddingInPx,
                numOfStars,
                stepSize,
                starSizeInPx
            )

            if (direction == LayoutDirection.Rtl)
                calculatedStars = numOfStars - calculatedStars
            onValueChange(calculatedStars)
        }.onPointerEvent(PointerEventType.Move) { event ->
            if (isIndicator || hideInactiveStars)
                return@onPointerEvent
            val change = event.changes.last()

            var calculatedStars = RatingBarUtils.calculateStars(
                change.position.x,
                paddingInPx,
                numOfStars,
                stepSize,
                starSizeInPx
            )

            if (direction == LayoutDirection.Rtl)
                calculatedStars = numOfStars - calculatedStars
            onValueChange(calculatedStars)
        }.onPointerEvent(PointerEventType.Exit) { event ->
            if (isIndicator || hideInactiveStars)
                return@onPointerEvent
            hover = false
        }.onPointerEvent(PointerEventType.Press) { event ->
            if (isIndicator || hideInactiveStars)
                return@onPointerEvent

            if (hover) {
                val change = event.changes.last()

                var calculatedStars = RatingBarUtils.calculateStars(
                    change.position.x,
                    paddingInPx,
                    numOfStars,
                    stepSize,
                    starSizeInPx
                )

                if (direction == LayoutDirection.Rtl)
                    calculatedStars = numOfStars - calculatedStars
                onRatingChanged(calculatedStars)
            }
        }

    ) {
        ComposeStars(
            if (hover) value else initialValue,
            hover,
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