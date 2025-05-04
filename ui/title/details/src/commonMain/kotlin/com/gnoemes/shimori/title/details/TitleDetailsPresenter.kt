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
import androidx.paging.compose.collectAsLazyPagingItems
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.compose.isExpanded
import com.gnoemes.shimori.common.compose.isMedium
import com.gnoemes.shimori.common.ui.overlay.showInSideSheet
import com.gnoemes.shimori.common.ui.resources.strings.title_link_not_found
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.eventbus.EventBus
import com.gnoemes.shimori.data.events.AppUiEvents
import com.gnoemes.shimori.data.events.TitleUiEvents
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.domain.interactors.UpdateTitle
import com.gnoemes.shimori.domain.interactors.UpdateTitlePersons
import com.gnoemes.shimori.domain.observers.ObserveAnimeScreenshotsCount
import com.gnoemes.shimori.domain.observers.ObserveAnimeStudios
import com.gnoemes.shimori.domain.observers.ObserveTitleGenres
import com.gnoemes.shimori.domain.observers.ObserveTitlePersons
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import com.gnoemes.shimori.domain.onFailurePublishToBus
import com.gnoemes.shimori.screens.PersonDetailsScreen
import com.gnoemes.shimori.screens.TitleCharactersScreen
import com.gnoemes.shimori.screens.TitleDetailsScreen
import com.gnoemes.shimori.screens.TitleFramesScreen
import com.gnoemes.shimori.screens.TitleTrailersScreen
import com.gnoemes.shimori.screens.TrackEditScreen
import com.gnoemes.shimori.screens.UrlScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource

@Inject
@CircuitInject(screen = TitleDetailsScreen::class, UiScope::class)
class TitleDetailsPresenter(
    @Assisted private val screen: TitleDetailsScreen,
    @Assisted private val navigator: Navigator,
    private val updateTitle: Lazy<UpdateTitle>,
    private val updateTitlePersons: Lazy<UpdateTitlePersons>,
    private val observeTitleWithTrack: Lazy<ObserveTitleWithTrackEntity>,
    private val observeGenres: Lazy<ObserveTitleGenres>,
    private val observePersons: Lazy<ObserveTitlePersons>,
    private val observeAnimeScreenshotsCount: Lazy<ObserveAnimeScreenshotsCount>,
    private val observeAnimeStudios: Lazy<ObserveAnimeStudios>,
) : Presenter<TitleDetailsUiState> {

    @Composable
    override fun present(): TitleDetailsUiState {
        val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
        val isCompact by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isCompact() } }
        val isMedium by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isMedium() } }
        val isExpanded by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isExpanded() } }

        val titleWithEntity by observeTitleWithTrack.value.flow.collectAsState(null)
        val genres by observeGenres.value.flow.collectAsState(emptyList())
        val isFramesExists =
            //avoid lazy initialization
            if (screen.type.anime) observeAnimeScreenshotsCount.value.flow.map { it > 0 }
                .collectAsState(false)
            else mutableStateOf(false)

        val persons = observePersons.value.flow.collectAsLazyPagingItems()

        val studios =
            if (screen.type.anime) observeAnimeStudios.value.flow.collectAsState(emptyList())
            else mutableStateOf(emptyList())


        val isTranslatorsExists = remember {
            derivedStateOf {
                val title = titleWithEntity?.entity
                if (title is Anime) {
                    !title.dubbers.isNullOrEmpty() || !title.subbers.isNullOrEmpty()
                } else false
            }
        }
        var descriptionExpanded by remember(isExpanded) { mutableStateOf(isExpanded) }
        var isShowCharacters by remember { mutableStateOf(true) }
        var isShowTrailers by remember { mutableStateOf(screen.type.anime) }

        val scope = rememberCoroutineScope()
        val overlayHost = LocalOverlayHost.current

        LaunchedEffect(Unit) {
            launchOrThrow {
                updateTitle.value(UpdateTitle.Params.optionalUpdate(screen.id, screen.type))
                    .onFailurePublishToBus()
            }

            launchOrThrow {
                updateTitlePersons.value(
                    UpdateTitlePersons.Params.optionalUpdate(
                        screen.id,
                        screen.type
                    )
                )
                    .onFailurePublishToBus()
            }

            observeTitleWithTrack.value(
                ObserveTitleWithTrackEntity.Params(screen.id, screen.type)
            )

            observeGenres.value(
                ObserveTitleGenres.Params(screen.id, screen.type)
            )

            observePersons.value(
                ObserveTitlePersons.Params(screen.id, screen.type, PAGING_CONFIG)
            )

            if (screen.type.anime) {
                observeAnimeScreenshotsCount.value(
                    ObserveAnimeScreenshotsCount.Params(screen.id)
                )

                observeAnimeStudios.value(
                    ObserveAnimeStudios.Params(screen.id)
                )
            }
        }

        val linkError = stringResource(Strings.title_link_not_found)

        val eventSink: CoroutineScope.(TitleDetailsUiEvent) -> Unit = { event ->
            when (event) {
                TitleDetailsUiEvent.NavigateUp -> navigator.pop()
                TitleDetailsUiEvent.Share -> TODO()
                TitleDetailsUiEvent.ExpandDescription -> descriptionExpanded = true


                TitleDetailsUiEvent.ToggleFavorite -> TODO()

                TitleDetailsUiEvent.OpenCharactersList -> {
                    titleWithEntity?.entity?.let { title ->
                        navigator.goTo(
                            TitleCharactersScreen(
                                title.id,
                                title.type,
                                asContent = false
                            )
                        )
                    }
                }

                TitleDetailsUiEvent.OpenChronology -> TODO()
                TitleDetailsUiEvent.OpenFrames -> titleWithEntity?.let { title ->
                    navigator.goTo(
                        TitleFramesScreen(title.id)
                    )
                }

                TitleDetailsUiEvent.OpenInBrowser -> {
                    titleWithEntity?.entity?.url?.let { url ->
                        navigator.goTo(UrlScreen(url))
                    } ?: launchOrThrow {
                        EventBus.publish(AppUiEvents.UiMessage(linkError))
                    }
                }

                TitleDetailsUiEvent.OpenTrailers -> {
                    titleWithEntity?.entity?.let { title ->
                        navigator.goTo(
                            TitleTrailersScreen(
                                title.id,
                                title.type,
                                asContent = false
                            )
                        )
                    }
                }

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

                is TitleDetailsUiEvent.OpenPerson -> {
                    navigator.goTo(PersonDetailsScreen(event.id))
                }

                is TitleDetailsUiEvent.OpenGenreSearch -> {
                    //TODO open search
                }

                is TitleDetailsUiEvent.OpenStudioSearch -> {
                    //TODO open search
                }

                is TitleDetailsUiEvent.OpenTitle -> {
                    navigator.goTo(TitleDetailsScreen(event.id, event.type))
                }

            }
        }

        LaunchedEffect(Unit) {
            EventBus.observe<TitleUiEvents> {
                when (it) {
                    is TitleUiEvents.HideCharacters -> isShowCharacters = false
                    is TitleUiEvents.HideTrailers -> isShowTrailers = false

                    else -> Unit
                }
            }
        }


        return TitleDetailsUiState(
            isListView = isCompact || isMedium,
            title = titleWithEntity?.entity,
            track = titleWithEntity?.track,
            genres = genres,
            isFavorite = titleWithEntity?.entity?.favorite ?: false,
            isShowTrailers = isShowTrailers,
            descriptionExpanded = descriptionExpanded,
            isShowCharacters = isShowCharacters,
            isFramesExists = isFramesExists.value,
            isTranslatorsExists = isTranslatorsExists.value,
            studios = studios.value,
            persons = persons,
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