package com.gnoemes.shimori.title.details

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class TitleDetailsUiState(
    val isListView: Boolean,
    val title: ShimoriTitleEntity?,
    val track: Track?,
    val genres: List<Genre>,
    val isFavorite: Boolean,
    val descriptionExpanded: Boolean,
    val isShowCharacters: Boolean,
    val isShowTrailers: Boolean,
    val isFramesExists: Boolean,
    val eventSink: (TitleDetailsUiEvent) -> Unit
) : CircuitUiState

sealed interface TitleDetailsUiEvent : CircuitUiEvent {
    data object ToggleFavorite : TitleDetailsUiEvent
    data object Share : TitleDetailsUiEvent
    data object ExpandDescription : TitleDetailsUiEvent

    data object OpenInBrowser : TitleDetailsUiEvent
    data object OpenCharactersList : TitleDetailsUiEvent
    data object OpenFrames : TitleDetailsUiEvent
    data object OpenTrailers : TitleDetailsUiEvent
    data object OpenTranslators : TitleDetailsUiEvent
    data object OpenChronology : TitleDetailsUiEvent
    data object NavigateUp : TitleDetailsUiEvent

    data class OpenTrailer(val id: Long) : TitleDetailsUiEvent
    data class OpenEditTrack(val id: Long, val type: TrackTargetType) : TitleDetailsUiEvent
    data class OpenGenreSearch(val id: Long) : TitleDetailsUiEvent
    data class OpenStudioSearch(val studioName: String) : TitleDetailsUiEvent
    data class OpenHuman(val id: Long) : TitleDetailsUiEvent
    data class OpenTitle(val id: Long, val type: TrackTargetType) : TitleDetailsUiEvent
}