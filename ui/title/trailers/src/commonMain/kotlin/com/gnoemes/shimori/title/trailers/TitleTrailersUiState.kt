package com.gnoemes.shimori.title.trailers

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class TitleTrailersUiState(
    val asContent: Boolean,
    val titleName: String,
    val trailers: List<AnimeVideo>?,
    val eventSink: (TitleTrailersUiEvent) -> Unit
) : CircuitUiState

sealed interface TitleTrailersUiEvent : CircuitUiEvent {

    data object NavigateUp : TitleTrailersUiEvent

    data class OpenTrailer(val id: Long) : TitleTrailersUiEvent
}