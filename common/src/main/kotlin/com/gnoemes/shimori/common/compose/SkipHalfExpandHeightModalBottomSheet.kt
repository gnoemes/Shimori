package com.gnoemes.shimori.common.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.gnoemes.shimori.common.compose.material.*


/**
 * My version with fixed initial height
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SkipHalfExpandHeightModalBottomSheetLayout(
    modifier: Modifier = Modifier,
    sheetContent : @Composable ColumnScope.() -> Unit,
    sheetState : ModalBottomSheetState = rememberSkipHalfExpandModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = sheetContent,
            modifier = modifier,
            sheetShape = sheetShape,
            sheetElevation = sheetElevation,
            sheetBackgroundColor = sheetBackgroundColor,
            sheetContentColor = sheetContentColor,
            scrimColor = scrimColor,
            content = content
    )
}

@Composable
@ExperimentalMaterialApi
fun rememberSkipHalfExpandModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = {
        when(it) {
            ModalBottomSheetValue.HalfExpanded -> false
            else -> true
        }
    }
): ModalBottomSheetState {
    return rememberSaveable(
            saver = ModalBottomSheetState.Saver(
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange
            )
    ) {
        ModalBottomSheetState(
                initialValue = initialValue,
                animationSpec = animationSpec,
                confirmStateChange = confirmStateChange
        )
    }
}