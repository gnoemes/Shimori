package com.gnoemes.shimori.common.ui.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive

@Composable
inline fun <E : CircuitUiEvent> wrapEventSink(
    crossinline eventSink: CoroutineScope.(E) -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): (E) -> Unit = { event ->
    if (coroutineScope.isActive) {
        coroutineScope.eventSink(event)
    } else {

    }
}