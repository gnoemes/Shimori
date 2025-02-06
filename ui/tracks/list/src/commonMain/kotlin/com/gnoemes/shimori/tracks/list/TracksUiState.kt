package com.gnoemes.shimori.tracks.list

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.gnoemes.shimori.common.compose.ui.UiMessage
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class TracksUiState(
    val type: TrackTargetType,
    val status: TrackStatus,
    val sort : ListSort,
    val isMenuButtonVisible: Boolean,
    val isMenuVisible: Boolean,
    val sortOptions: List<ListSortOption>,
    val items: LazyPagingItems<TitleWithTrackEntity>,
    val firstSyncLoading: Boolean,
    val uiMessage: UiMessage?,
    val eventSink: (TracksUiEvent) -> Unit
) : CircuitUiState

sealed interface TracksUiEvent : CircuitUiEvent {
    data object OpenSettings : TracksUiEvent
    data object OpenMenu : TracksUiEvent
    data class AddOneToProgress(val track: Track) : TracksUiEvent
    data class OpenDetails(val title : TitleWithTrackEntity) : TracksUiEvent
    data class OpenEdit(val title : TitleWithTrackEntity) : TracksUiEvent
    data class ChangeSort(val sort: ListSort) : TracksUiEvent
}