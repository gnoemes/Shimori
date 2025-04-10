package com.gnoemes.shimori.title.characters

import androidx.compose.runtime.Immutable
import app.cash.paging.compose.LazyPagingItems
import com.gnoemes.shimori.data.characters.CharacterWithRole
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class TitleCharactersUiState(
    val isList: Boolean,
    val titleName: String,
    val isShowSearchButton: Boolean,
    val isSearchActive: Boolean,
    val characters: LazyPagingItems<CharacterWithRole>,
    val eventSink: (TitleCharactersUiEvent) -> Unit
) : CircuitUiState

sealed interface TitleCharactersUiEvent : CircuitUiEvent {

    data object OpenSearch : TitleCharactersUiEvent
    data object CloseSearch : TitleCharactersUiEvent
    data object NavigateUp : TitleCharactersUiEvent

    data class Search(val value: String) : TitleCharactersUiEvent
    data class OpenCharacter(val id: Long) : TitleCharactersUiEvent
}