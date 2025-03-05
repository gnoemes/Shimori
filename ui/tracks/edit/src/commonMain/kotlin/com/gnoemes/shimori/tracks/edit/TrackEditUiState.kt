package com.gnoemes.shimori.tracks.edit

import androidx.compose.runtime.Stable
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class TrackEditUiState(
    val isEdit: Boolean,
    val titleName: String,
    val type: TrackTargetType,
    val status: TrackStatus,
    val score: Int?,
    val progress: Int,
    val maxProgress: Int,
    val repeats: Int,
    val decrementEnabled: Boolean,
    val incrementEnabled: Boolean,
    val note: String?,
    val eventSink: (TrackEditUiEvent) -> Unit
) : CircuitUiState

sealed interface TrackEditUiEvent : CircuitUiEvent {
    data class ChangeStatus(val newValue: TrackStatus) : TrackEditUiEvent
    data class ChangeScore(val newValue: Int) : TrackEditUiEvent
    data class ChangeProgress(val newValue: Int?) : TrackEditUiEvent
    data object IncrementProgress : TrackEditUiEvent
    data object DecrementProgress : TrackEditUiEvent
    data class ChangeRepeats(val newValue: Int?) : TrackEditUiEvent
    data class ChangeNote(val newValue: String?) : TrackEditUiEvent
    data object Save : TrackEditUiEvent
    data object Delete : TrackEditUiEvent
    data object NavigateUp : TrackEditUiEvent
}