package com.gnoemes.shimori.title.characters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.rememberRetainedCachedPagingFlow
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.characters.CharacterWithRole
import com.gnoemes.shimori.data.eventbus.EventBus
import com.gnoemes.shimori.data.events.TitleUiEvents
import com.gnoemes.shimori.domain.interactors.UpdateTitleCharacters
import com.gnoemes.shimori.domain.observers.ObserveTitleCharacters
import com.gnoemes.shimori.domain.observers.ObserveTitleCharactersCount
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import com.gnoemes.shimori.domain.onFailurePublishToBus
import com.gnoemes.shimori.screens.CharacterDetailsScreen
import com.gnoemes.shimori.screens.TitleCharactersScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TitleCharactersScreen::class, UiScope::class)
class TitleCharactersPresenter(
    @Assisted private val screen: TitleCharactersScreen,
    @Assisted private val navigator: Navigator,
    private val updateTitleCharacters: Lazy<UpdateTitleCharacters>,
    private val observeTitleCharacters: Lazy<ObserveTitleCharacters>,
    private val observeTitle: Lazy<ObserveTitleWithTrackEntity>,
    private val observeCharactersCount: Lazy<ObserveTitleCharactersCount>,
) : Presenter<TitleCharactersUiState> {

    @Composable
    override fun present(): TitleCharactersUiState {
        val isGrid = screen.grid

        val title by observeTitle.value.flow
            .mapNotNull { it?.entity }
            .distinctUntilChangedBy { it.id }
            .collectAsState(null)

        val titleNameLocalized = LocalShimoriTextCreator.current.nullable {
            title?.name()
        } ?: ""

        val titleName by remember { derivedStateOf { titleNameLocalized } }
        val charactersCount by observeCharactersCount.value.flow.collectAsState(-1)
        val charactersUpdating by updateTitleCharacters.value.inProgress.collectAsState(false)

        val retainedCharacterPagingInteractor =
            rememberRetained(screen.id, screen.type) { observeTitleCharacters.value }
        val charactersFlow = remember(screen.id, screen.type) {
            retainedCharacterPagingInteractor.flow
                .filterIsInstance<PagingData<CharacterWithRole>>()
        }

        val characters = charactersFlow
            .rememberRetainedCachedPagingFlow()
            .collectAsLazyPagingItems()

        var searchInput by remember { mutableStateOf("") }
        var isSearchActive by remember { mutableStateOf(false) }

        if (isGrid) {
            LaunchedEffect(Unit) {
                launchOrThrow {
                    updateTitleCharacters.value(
                        UpdateTitleCharacters.Params.optionalUpdate(
                            screen.id,
                            screen.type
                        )
                    ).onFailurePublishToBus()
                }

                observeTitle.value(
                    ObserveTitleWithTrackEntity.Params(screen.id, screen.type)
                )

                observeCharactersCount.value(
                    ObserveTitleCharactersCount.Params(screen.id, screen.type)
                )
            }
        }

        LaunchedEffect(searchInput) {
            observeTitleCharacters.value(
                ObserveTitleCharacters.Params(screen.id, screen.type, searchInput, PAGING_CONFIG)
            )
        }

        val hideList = !isGrid && !charactersUpdating && charactersCount == 0
        LaunchedEffect(hideList) {
            if (hideList) {
                EventBus.publish(TitleUiEvents.HideCharacters)
            }
        }

        val eventSink: CoroutineScope.(TitleCharactersUiEvent) -> Unit = { event ->
            when (event) {
                TitleCharactersUiEvent.NavigateUp -> navigator.pop()
                TitleCharactersUiEvent.CloseSearch -> isSearchActive = false
                TitleCharactersUiEvent.OpenSearch -> isSearchActive = true
                is TitleCharactersUiEvent.Search -> searchInput = event.value
                is TitleCharactersUiEvent.OpenCharacter -> navigator.goTo(
                    CharacterDetailsScreen(
                        event.id
                    )
                )
            }
        }

        return TitleCharactersUiState(
            isList = !isGrid,
            titleName = titleName,
            isShowSearchButton = isGrid && !isSearchActive,
            isSearchActive = isSearchActive,
            characters = characters,
            eventSink = wrapEventSink(eventSink)
        )
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
        )
    }
}