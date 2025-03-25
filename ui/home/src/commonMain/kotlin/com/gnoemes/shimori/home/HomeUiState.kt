package com.gnoemes.shimori.home

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.common.compose.ui.UiMessage
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class HomeUiState(
    val fabSpacing : Boolean,
    val message : UiMessage?,
    val isAuthorized: Boolean,
    val profileImage: ShimoriImage?,
    val eventSink: (HomeUiEvent) -> Unit
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
    data class OnNavEvent(val navEvent: NavEvent) : HomeUiEvent
    data class OpenTrackEdit(
        val targetId: Long,
        val targetType: TrackTargetType,
        val predefinedStatus: TrackStatus? = null
    ) : HomeUiEvent

    data class ClearMessage(val id: Long) : HomeUiEvent
    data class ActionMessage(val message: UiMessage) : HomeUiEvent

    data object Logout : HomeUiEvent
}