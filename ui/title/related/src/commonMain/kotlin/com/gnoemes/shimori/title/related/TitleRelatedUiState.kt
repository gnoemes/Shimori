package com.gnoemes.shimori.title.related

import androidx.compose.runtime.Immutable
import app.cash.paging.compose.LazyPagingItems
import com.gnoemes.shimori.data.common.Related
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class TitleRelatedUiState(
    val isList: Boolean,
    val related: LazyPagingItems<Related>,
    val eventSink: (TitleRelatedUiEvent) -> Unit
) : CircuitUiState

sealed interface TitleRelatedUiEvent : CircuitUiEvent {

    data object NavigateUp : TitleRelatedUiEvent
    data class OpenTitle(val targetId: Long, val targetType: TrackTargetType) : TitleRelatedUiEvent
    data class OpenEdit(val targetId: Long, val targetType: TrackTargetType) : TitleRelatedUiEvent

}