package com.gnoemes.shimori.common.ui.overlay

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Dp
import com.gnoemes.shimori.common.compose.ui.ModalSideSheet
import com.gnoemes.shimori.common.compose.ui.SideSheetDefaults
import com.gnoemes.shimori.common.compose.ui.rememberModalSideSheetState
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class SideSheetOverlay<Model : Any, Result : Any>(
    private val model: Model,
    private val onDismiss: () -> Result,
    private val tonalElevation: Dp = SideSheetDefaults.Elevation,
    private val scrimColor: Color = Color.Unspecified,
    private val content: @Composable (Model, OverlayNavigator<Result>) -> Unit,
) : Overlay<Result> {
    @Composable
    override fun Content(navigator: OverlayNavigator<Result>) {
        val sheetState = rememberModalSideSheetState()

        val coroutineScope = rememberCoroutineScope()
        BackHandler(enabled = sheetState.isVisible) {
            coroutineScope
                .launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        navigator.finish(onDismiss())
                    }
                }
        }

        ModalSideSheet(
            content = {
                // Delay setting the result until we've finished dismissing
                content(model) { result ->
                    // This is the OverlayNavigator.finish() callback
                    coroutineScope.launch {
                        try {
                            sheetState.hide()
                        } finally {
                            navigator.finish(result)
                        }
                    }
                }
            },
            tonalElevation = tonalElevation,
            scrimColor = if (scrimColor.isSpecified) scrimColor else SideSheetDefaults.ScrimColor,
            sheetState = sheetState,
            onDismissRequest = { navigator.finish(onDismiss()) },
        )

        LaunchedEffect(Unit) { sheetState.show() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
suspend fun OverlayHost.showInSideSheet(
    screen: Screen,
    tonalElevation: Dp = SideSheetDefaults.Elevation,
    scrimColor: Color = Color.Unspecified,
    hostNavigator: Navigator? = null,
): Unit = show(
    SideSheetOverlay(
        model = Unit,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        onDismiss = {},
    ) { _, navigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> navigator.finish(Unit)
                    else -> hostNavigator?.onNavEvent(event)
                }
            },
        )
    },
)