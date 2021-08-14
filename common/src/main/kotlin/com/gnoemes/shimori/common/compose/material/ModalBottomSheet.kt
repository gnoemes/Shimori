package com.gnoemes.shimori.common.compose.material

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.material.ModalBottomSheetValue.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Possible values of [ModalBottomSheetState].
 */
@ExperimentalMaterialApi
enum class ModalBottomSheetValue {
    /**
     * The bottom sheet is not visible.
     */
    Hidden,

    /**
     * The bottom sheet is visible at full height.
     */
    Expanded,

    /**
     * The bottom sheet is partially visible at 50% of the screen height. This state is only
     * enabled if the height of the bottom sheet is more than 50% of the screen height.
     */
    HalfExpanded
}

/**
 * State of the [ModalBottomSheetLayout] composable.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@ExperimentalMaterialApi
class ModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = { true }
) : SwipeableState<ModalBottomSheetValue>(
        initialValue = initialValue,
        animationSpec = animationSpec,
        confirmStateChange = confirmStateChange
) {
    /**
     * Whether the bottom sheet is visible.
     */
    val isVisible: Boolean
        get() = currentValue != Hidden

    internal val isHalfExpandedEnabled: Boolean
        get() = anchors.values.contains(HalfExpanded)

    /**
     * Show the bottom sheet with animation and suspend until it's shown. If half expand is
     * enabled, the bottom sheet will be half expanded. Otherwise it will be fully expanded.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun show() {
        val targetValue =
            if (isHalfExpandedEnabled) HalfExpanded
            else Expanded
        animateTo(targetValue = targetValue)
    }

    /**
     * Half expand the bottom sheet if half expand is enabled with animation and suspend until it
     * animation is complete or cancelled
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    internal suspend fun halfExpand() {
        if (!isHalfExpandedEnabled) {
            return
        }
        animateTo(HalfExpanded)
    }

    /**
     * Fully expand the bottom sheet with animation and suspend until it if fully expanded or
     * animation has been cancelled.
     * *
     * @throws [CancellationException] if the animation is interrupted
     */
    internal suspend fun expand() = animateTo(Expanded)

    /**
     * Hide the bottom sheet with animation and suspend until it if fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun hide() = animateTo(Hidden)

    internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection

    companion object {
        /**
         * The default [Saver] implementation for [ModalBottomSheetState].
         */
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (ModalBottomSheetValue) -> Boolean
        ): Saver<ModalBottomSheetState, *> = Saver(
                save = { it.currentValue },
                restore = {
                    ModalBottomSheetState(
                            initialValue = it,
                            animationSpec = animationSpec,
                            confirmStateChange = confirmStateChange
                    )
                }
        )
    }
}

/**
 * Create a [ModalBottomSheetState] and [remember] it.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
@ExperimentalMaterialApi
fun rememberSkipHalfExpandModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = { true }
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

/**
 * <a href="https://material.io/components/sheets-bottom#modal-bottom-sheet" class="external" target="_blank">Material Design modal bottom sheet</a>.
 *
 *
 * EDITED: DELETED HALF EXPAND STATE FROM ANCHORS [Modifier.bottomSheetSwipeable]
 *
 * Modal bottom sheets present a set of choices while blocking interaction with the rest of the
 * screen. They are an alternative to inline menus and simple dialogs, providing
 * additional room for content, iconography, and actions.
 *
 * ![Modal bottom sheet image](https://developer.android.com/images/reference/androidx/compose/material/modal-bottom-sheet.png)
 *
 * A simple example of a modal bottom sheet looks like this:
 *
 * @sample androidx.compose.material.samples.ModalBottomSheetSample
 *
 * @param sheetContent The content of the bottom sheet.
 * @param modifier Optional [Modifier] for the entire component.
 * @param sheetState The state of the bottom sheet.
 * @param sheetShape The shape of the bottom sheet.
 * @param sheetElevation The elevation of the bottom sheet.
 * @param sheetBackgroundColor The background color of the bottom sheet.
 * @param sheetContentColor The preferred content color provided by the bottom sheet to its
 * children. Defaults to the matching content color for [sheetBackgroundColor], or if that is not
 * a color from the theme, this will keep the same content color set above the bottom sheet.
 * @param scrimColor The color of the scrim that is applied to the rest of the screen when the
 * bottom sheet is visible. If the color passed is [Color.Unspecified], then a scrim will no
 * longer be applied and the bottom sheet will not block interaction with the rest of the screen
 * when visible.
 * @param content The content of rest of the screen.
 */
@Composable
@ExperimentalMaterialApi
fun ModalBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState =
        rememberSkipHalfExpandModalBottomSheetState(Hidden),
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints(modifier) {
        val fullHeight = constraints.maxHeight.toFloat()
        val sheetHeightState = remember { mutableStateOf<Float?>(null) }
        Box(Modifier.fillMaxSize()) {
            content()
            Scrim(
                    color = scrimColor,
                    onDismiss = {
                        if (sheetState.confirmStateChange(Hidden)) {
                            scope.launch { sheetState.hide() }
                        }
                    },
                    visible = sheetState.targetValue != Hidden
            )
        }
        Surface(
                Modifier
                    .fillMaxWidth()
                    .nestedScroll(sheetState.nestedScrollConnection)
                    .offset {
                        val y = if (sheetState.anchors.isEmpty()) {
                            // if we don't know our anchors yet, render the sheet as hidden
                            fullHeight.roundToInt()
                        } else {
                            // if we do know our anchors, respect them
                            sheetState.offset.value.roundToInt()
                        }
                        IntOffset(0, y)
                    }
                    .bottomSheetSwipeable(sheetState, fullHeight, sheetHeightState)
                    .onGloballyPositioned {
                        sheetHeightState.value = it.size.height.toFloat()
                    }
                    .semantics {
                        if (sheetState.isVisible) {
                            dismiss {
                                if (sheetState.confirmStateChange(Hidden)) {
                                    scope.launch { sheetState.hide() }
                                }
                                true
                            }
                            if (sheetState.currentValue == HalfExpanded) {
                                expand {
                                    if (sheetState.confirmStateChange(Expanded)) {
                                        scope.launch { sheetState.expand() }
                                    }
                                    true
                                }
                            } else if (sheetState.isHalfExpandedEnabled) {
                                collapse {
                                    if (sheetState.confirmStateChange(HalfExpanded)) {
                                        scope.launch { sheetState.halfExpand() }
                                    }
                                    true
                                }
                            }
                        }
                    },
                shape = sheetShape,
                elevation = sheetElevation,
                color = sheetBackgroundColor,
                contentColor = sheetContentColor
        ) {
            Column(content = sheetContent)
        }
    }
}


@Suppress("ModifierInspectorInfo")
@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.bottomSheetSwipeable(
    sheetState: ModalBottomSheetState,
    fullHeight: Float,
    sheetHeightState: State<Float?>
): Modifier {
    val sheetHeight = sheetHeightState.value
    val modifier = if (sheetHeight != null) {
        val anchors = if (sheetHeight < fullHeight / 2) {
            mapOf(
                    fullHeight to Hidden,
                    fullHeight - sheetHeight to Expanded
            )
        } else {
            mapOf(
                    fullHeight to Hidden,
                    max(0f, fullHeight - sheetHeight) to Expanded
            )
        }
        Modifier.swipeable(
                state = sheetState,
                anchors = anchors,
                orientation = Orientation.Vertical,
                enabled = sheetState.currentValue != Hidden,
                resistance = null
        )
    } else {
        Modifier
    }

    return this.then(modifier)
}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                animationSpec = TweenSpec()
        )
        val dismissModifier = if (visible) {
            Modifier
                .pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
                .semantics(mergeDescendants = true) {
                    onClick { onDismiss(); true }
                }
        } else {
            Modifier
        }

        Canvas(
                Modifier
                    .fillMaxSize()
                    .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

/**
 * Contains useful Defaults for [ModalBottomSheetLayout].
 */
object ModalBottomSheetDefaults {

    /**
     * The default elevation used by [ModalBottomSheetLayout].
     */
    val Elevation = 16.dp

    /**
     * The default scrim color used by [ModalBottomSheetLayout].
     */
    val scrimColor: Color
        @Composable
        get() = MaterialTheme.colors.onSurface.copy(alpha = 0.32f)
}