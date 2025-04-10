package com.gnoemes.shimori.title.characters

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.NestedScaffold
import com.gnoemes.shimori.common.compose.rememberLazyListState
import com.gnoemes.shimori.common.compose.theme.CharacterCoverRoundedCornerShape
import com.gnoemes.shimori.common.compose.ui.CharacterCover
import com.gnoemes.shimori.common.compose.ui.CharacterItem
import com.gnoemes.shimori.common.compose.ui.TransparentToolbar
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_back
import com.gnoemes.shimori.data.characters.CharacterWithRole
import com.gnoemes.shimori.screens.TitleCharactersScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.painterResource

@Composable
@CircuitInject(screen = TitleCharactersScreen::class, scope = UiScope::class)
internal fun TitleDetailsUi(
    state: TitleCharactersUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    TitleCharactersUi(
        isList = state.isList,
        titleName = state.titleName,
        isLoading = state.isLoading,
        isShowSearchButton = state.isShowSearchButton,
        isSearchActive = state.isSearchActive,
        characters = state.characters,
        search = { eventSink(TitleCharactersUiEvent.Search(it)) },
        openSearch = { eventSink(TitleCharactersUiEvent.OpenSearch) },
        closeSearch = { eventSink(TitleCharactersUiEvent.CloseSearch) },
        navigateUp = { eventSink(TitleCharactersUiEvent.NavigateUp) },
        openCharacter = { eventSink(TitleCharactersUiEvent.OpenCharacter(it)) },
    )
}

@Composable
private fun TitleCharactersUi(
    isList: Boolean,
    titleName: String,
    isLoading: Boolean,
    isShowSearchButton: Boolean,
    isSearchActive: Boolean,
    characters: LazyPagingItems<CharacterWithRole>,
    openSearch: () -> Unit,
    closeSearch: () -> Unit,
    navigateUp: () -> Unit,
    search: (String) -> Unit,
    openCharacter: (Long) -> Unit,
) {
    if (isList) {
        TitleCharactersUiListContent(characters, isLoading, openCharacter)
    } else {
        TitleCharactersUiGridContent(
            titleName,
            isLoading,
            isShowSearchButton,
            isSearchActive,
            characters,
            openSearch,
            closeSearch,
            navigateUp,
            search,
            openCharacter
        )
    }
}


@Composable
private fun TitleCharactersUiListContent(
    characters: LazyPagingItems<CharacterWithRole>,
    isLoading: Boolean,
    openCharacter: (Long) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = characters.rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(Modifier.width(0.dp)) }

        if (characters.itemCount == 0) {
            items(10) {
                CharacterCover(
                    null,
                    modifier = Modifier.height(176.dp)
                        .aspectRatio(3 / 4f)
                        .placeholder(
                            visible = true,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            highlight = PlaceholderHighlight.shimmer(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.52f)
                            ),
                            shape = CharacterCoverRoundedCornerShape,
                        ),
                    onClick = {}
                )
            }
        }

        items(
            count = characters.itemCount,
            key = characters.itemKey { "character_${it.id}" },
        ) { index ->
            val character = characters[index]
            if (character != null) {
                val openCharacterClick = remember(character.id) {
                    { openCharacter(character.id) }
                }

                CharacterItem(character.entity, coverHeight = 176.dp, onClick = openCharacterClick)
            }
        }

        item { Spacer(Modifier.width(0.dp)) }
    }
}

@Composable
private fun TitleCharactersUiGridContent(
    titleName: String,
    isLoading: Boolean,
    isShowSearchButton: Boolean,
    isSearchActive: Boolean,
    characters: LazyPagingItems<CharacterWithRole>,
    openSearch: () -> Unit,
    closeSearch: () -> Unit,
    navigateUp: () -> Unit,
    search: (String) -> Unit,
    openCharacter: (Long) -> Unit,
) {
    NestedScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TransparentToolbar(
                navigationIcon = {
                    IconButton(navigateUp) {
                        Icon(painterResource(Icons.ic_back), contentDescription = null)
                    }
                },
                onNavigationClick = navigateUp,
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .animateContentSize(),
                    ) {
                        AnimatedContent(isSearchActive) {
                            if (isSearchActive) {


                            } else {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .align(Alignment.CenterStart)
                                ) {
                                    Text(
                                        titleName,
                                        modifier = Modifier.fillMaxWidth(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        titleName,
                                        modifier = Modifier.fillMaxWidth(),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                },
            )
        }
    ) { paddingValue ->

    }
}

