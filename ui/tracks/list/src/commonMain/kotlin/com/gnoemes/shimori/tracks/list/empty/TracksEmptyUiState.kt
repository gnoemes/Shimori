package com.gnoemes.shimori.tracks.list.empty

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class TrackEmptyUiState(
    val availableTypes: List<TrackTargetType>,
    val eventSink: (TracksEmptyUiEvent) -> Unit
) : CircuitUiState

sealed interface TracksEmptyUiEvent : CircuitUiEvent {
    data class OpenExplore(val type: TrackTargetType) : TracksEmptyUiEvent
}