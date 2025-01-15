package com.gnoemes.shimori.tracks.list

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class TracksUiState(
    val eventSink: (TracksUiEvent) -> Unit
) : CircuitUiState

sealed interface TracksUiEvent : CircuitUiEvent {
    data object OpenSettings : TracksUiEvent
}