package com.gnoemes.shimori.common.compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun ModalSideSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalSideSheetState(),
    sheetMaxWidth: Dp = SideSheetDefaults.SheetMaxWidth,
    shape: Shape = SideSheetDefaults.ExpandedShape,
    containerColor: Color = SideSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = SideSheetDefaults.ScrimColor,
    contentWindowInsets: @Composable () -> WindowInsets = { SideSheetDefaults.windowInsets },
    content: @Composable ColumnScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()
    val animateToDismiss: () -> Unit = {
        scope
            .launch { sheetState.hide() }
            .invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismissRequest()
                }
            }
    }

    val predictiveBackProgress = remember { Animatable(initialValue = 0f) }

    ModalSideSheet(
        onDismissRequest = {
            if (sheetState.currentValue == Expanded) {
                scope.launch { predictiveBackProgress.animateTo(0f) }
            } else {
                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissRequest() }
            }
        },
        predictiveBackProgress = predictiveBackProgress,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Scrim(
                color = scrimColor,
                onDismissRequest = animateToDismiss,
                visible = sheetState.targetValue != Hidden
            )

            AnimatedVisibility(
                visible = sheetState.isVisible,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth * 2 },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth * 2 },
                    animationSpec = tween(durationMillis = 300)
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                ModalSideSheetContent(
                    predictiveBackProgress,
                    scope,
                    animateToDismiss,
                    modifier,
                    sheetState,
                    sheetMaxWidth,
                    shape,
                    containerColor,
                    contentColor,
                    tonalElevation,
                    contentWindowInsets,
                    content
                )
            }
        }
    }

    LaunchedEffect(sheetState) { sheetState.show() }
}

@Composable
@ExperimentalMaterial3Api
internal fun BoxScope.ModalSideSheetContent(
    predictiveBackProgress: Animatable<Float, AnimationVector1D>,
    scope: CoroutineScope,
    animateToDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = SideSheetDefaults.SheetMaxWidth,
    shape: Shape = SideSheetDefaults.ExpandedShape,
    containerColor: Color = SideSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = SideSheetDefaults.Elevation,
    contentWindowInsets: @Composable () -> WindowInsets = { SideSheetDefaults.windowInsets },
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .widthIn(max = sheetMaxWidth)
            .fillMaxWidth(),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .windowInsetsPadding(contentWindowInsets())
                .verticalScroll(rememberScrollState())
                .semantics(
                    mergeDescendants = true
                ) {
                    with(sheetState) {
                        dismiss("46") {
                            animateToDismiss()
                            true
                        }
                    }
                },
            content = content
        )
    }
}


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
internal fun ModalSideSheet(
    onDismissRequest: () -> Unit,
    predictiveBackProgress: Animatable<Float, AnimationVector1D>,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
            usePlatformInsets = false,
            scrimColor = Color.Transparent
        ),
        content = content
    )
}

@ExperimentalMaterial3Api
@Composable
fun rememberModalSideSheetState(
    confirmValueChange: (SheetValue) -> Boolean = { true },
) =
    rememberSheetState(
        confirmValueChange = confirmValueChange,
        initialValue = Hidden,
    )


@Composable
private fun Scrim(color: Color, onDismissRequest: () -> Unit, visible: Boolean) {
    if (color.isSpecified) {
        val alpha by
        animateFloatAsState(targetValue = if (visible) 1f else 0f, animationSpec = TweenSpec())
        val dismissSheet =
            if (visible) {
                Modifier.pointerInput(onDismissRequest) { detectTapGestures { onDismissRequest() } }
                    .semantics(mergeDescendants = true) {
                        traversalIndex = 1f
                        onClick {
                            onDismissRequest()
                            true
                        }
                    }
            } else {
                Modifier
            }
        Canvas(Modifier.fillMaxSize().then(dismissSheet)) {
            drawRect(color = color, alpha = alpha.coerceIn(0f, 1f))
        }
    }
}

@Composable
@ExperimentalMaterial3Api
internal fun rememberSheetState(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    initialValue: SheetValue = Hidden,
    skipHiddenState: Boolean = false,
): SheetState {
    val density = LocalDensity.current
    return rememberSaveable(
        skipPartiallyExpanded,
        confirmValueChange,
        skipHiddenState,
        saver =
        SheetState.Saver(
            skipPartiallyExpanded = skipPartiallyExpanded,
            confirmValueChange = confirmValueChange,
            density = density,
            skipHiddenState = skipHiddenState,
        )
    ) {
        SheetState(
            skipPartiallyExpanded,
            density,
            initialValue,
            confirmValueChange,
            skipHiddenState,
        )
    }
}

@Stable
@ExperimentalMaterial3Api
object SideSheetDefaults {
    /** The default shape for bottom sheets in a [Hidden] state. */
    val HiddenShape: Shape
        @Composable get() = RoundedCornerShape(0.dp)

    /** The default shape for a side sheets in [Expanded] state. */
    val ExpandedShape: Shape
        @Composable get() = MaterialTheme.shapes.extraLarge.copy(
            bottomEnd = CornerSize(0),
            topEnd = CornerSize(0)
        )

    /** The default container color for a sheet. */
    val ContainerColor: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerLow

    /** The default elevation for a sheet. */
    val Elevation = 1.dp

    /** The default color of the scrim overlay for background content. */
    val ScrimColor: Color
        @Composable get() = ContainerColor.copy(alpha = .32f)

    /** The default max width used */
    val SheetMaxWidth = 320.dp

    /** Default insets to be used and consumed by content. */
    val windowInsets: WindowInsets
        @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Right)
}
