package com.gnoemes.shimori.title.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.compose.isExpanded
import com.gnoemes.shimori.common.compose.isMedium
import com.gnoemes.shimori.common.compose.rememberRetainedCachedPagingFlow
import com.gnoemes.shimori.common.ui.overlay.showInSideSheet
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.domain.interactors.UpdateTitle
import com.gnoemes.shimori.domain.observers.ObserveTitleCharacters
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import com.gnoemes.shimori.screens.TitleDetailsScreen
import com.gnoemes.shimori.screens.TrackEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TitleDetailsScreen::class, UiScope::class)
class TitleDetailsPresenter(
    @Assisted private val screen: TitleDetailsScreen,
    @Assisted private val navigator: Navigator,
    private val updateTitle: Lazy<UpdateTitle>,
    private val observeTitleWithTrack: Lazy<ObserveTitleWithTrackEntity>,
    private val observeTitleCharacters: Lazy<ObserveTitleCharacters>,
) : Presenter<TitleDetailsUiState> {

    @Composable
    override fun present(): TitleDetailsUiState {
        val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
        val isCompact by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isCompact() } }
        val isMedium by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isMedium() } }
        val isExpanded by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isExpanded() } }

        val titleWithEntity by observeTitleWithTrack.value.flow.collectAsState(null)

        val retainedCharacterPagingInteractor =
            rememberRetained(screen.id, screen.type) { observeTitleCharacters.value }
        val charactersFlow = remember(screen.id, screen.type) {
            retainedCharacterPagingInteractor.flow
                .filterIsInstance<PagingData<Character>>()
        }

        val characters = charactersFlow
            .rememberRetainedCachedPagingFlow()
            .collectAsLazyPagingItems()

        var descriptionExpanded by remember(isExpanded) { mutableStateOf(isExpanded) }

        val scope = rememberCoroutineScope()
        val overlayHost = LocalOverlayHost.current

        LaunchedEffect(Unit) {
            launchOrThrow {
                updateTitle.value(UpdateTitle.Params.optionalUpdate(screen.id, screen.type))
            }

            observeTitleWithTrack.value(
                ObserveTitleWithTrackEntity.Params(screen.id, screen.type)
            )

            observeTitleCharacters.value(
                ObserveTitleCharacters.Params(screen.id, screen.type, PAGING_CONFIG)
            )
        }

        val eventSink: CoroutineScope.(TitleDetailsUiEvent) -> Unit = { event ->
            when (event) {
                TitleDetailsUiEvent.NavigateUp -> navigator.pop()
                TitleDetailsUiEvent.Share -> TODO()
                TitleDetailsUiEvent.ExpandDescription -> {
                    descriptionExpanded = true
                }

                TitleDetailsUiEvent.ToggleFavorite -> TODO()

                TitleDetailsUiEvent.OpenCharactersList -> TODO()
                TitleDetailsUiEvent.OpenChronology -> TODO()
                TitleDetailsUiEvent.OpenFrames -> TODO()
                TitleDetailsUiEvent.OpenInBrowser -> TODO()
                TitleDetailsUiEvent.OpenTrailers -> TODO()
                TitleDetailsUiEvent.OpenTranslators -> TODO()
                is TitleDetailsUiEvent.OpenEditTrack -> {
                    val screen = TrackEditScreen(
                        event.id,
                        event.type,
                        predefinedStatus = null,
                    )

                    if (isCompact) navigator.goTo(screen)
                    else scope.launchOrThrow {
                        overlayHost.showInSideSheet(screen)
                    }
                }

                is TitleDetailsUiEvent.OpenHuman -> TODO()
                is TitleDetailsUiEvent.OpenGenreSearch -> TODO()
                is TitleDetailsUiEvent.OpenStudioSearch -> TODO()
                is TitleDetailsUiEvent.OpenTitle -> TODO()
                is TitleDetailsUiEvent.OpenTrailer -> TODO()
                is TitleDetailsUiEvent.OpenCharacter -> TODO()
            }
        }

        return TitleDetailsUiState(
            isListView = isCompact || isMedium,
            title = titleWithEntity?.entity,
            track = titleWithEntity?.track,
            isFavorite = titleWithEntity?.entity?.favorite ?: false,
            descriptionExpanded = descriptionExpanded,

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