package com.gnoemes.shimori.tracks.menu

import androidx.compose.runtime.Stable
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class TracksMenuUiState(
    val selectedType: TrackTargetType,
    val selectedStatus: TrackStatus,
    val availableStatuses: Map<TrackTargetType, List<TrackStatus>>,
    val eventSink: (TracksMenuUiEvent) -> Unit
) : CircuitUiState

sealed interface TracksMenuUiEvent : CircuitUiEvent {
    data class OpenStatus(val type: TrackTargetType, val status: TrackStatus) : TracksMenuUiEvent
}