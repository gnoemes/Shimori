package com.gnoemes.shimori.home

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.common.ShimoriImage
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class HomeUiState(
    val isAuthorized: Boolean,
    val profileImage: ShimoriImage?,
    val eventSink: (HomeUiEvent) -> Unit
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
    data class OnNavEvent(val navEvent: NavEvent) : HomeUiEvent
    data object Logout : HomeUiEvent
}