package com.gnoemes.shimori.common.ui.overlay

import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Dialog
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuit.runtime.screen.Screen

class DialogOverlay<Model : Any, Result : Any>(
    private val model: Model,
    private val onDismiss: () -> Result,
    private val content: @Composable (Model, OverlayNavigator<Result>) -> Unit,
) : Overlay<Result> {
    @Composable
    override fun Content(navigator: OverlayNavigator<Result>) {
        val coroutineScope = rememberCoroutineScope()
        Dialog(
            onDismissRequest = { navigator.finish(onDismiss()) },
        ) {
            Surface(
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor,
                tonalElevation = AlertDialogDefaults.TonalElevation,
            ) {
                // Delay setting the result until we've finished dismissing
                content(model) { result ->
                    // This is the OverlayNavigator.finish() callback
                    coroutineScope.launchOrThrow {
                        navigator.finish(result)
                    }
                }
            }
        }
    }
}

suspend fun OverlayHost.showInDialog(
    screen: Screen,
    onGoToScreen: (Screen) -> Unit,
): Unit = show(
    DialogOverlay(model = Unit, onDismiss = {}) { _, navigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> navigator.finish(Unit)
                    is NavEvent.GoTo -> onGoToScreen(event.screen)
                    else -> Unit
                }
            },
        )
    },
)