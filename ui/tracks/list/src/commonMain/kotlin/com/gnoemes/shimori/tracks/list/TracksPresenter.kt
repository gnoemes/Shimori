package com.gnoemes.shimori.tracks.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.gnoemes.shimori.common.compose.ui.UiMessageManager
import com.gnoemes.shimori.common.ui.overlay.showInBottomSheet
import com.gnoemes.shimori.common.ui.overlay.showInSideSheet
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.lists.ListsStateBus
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.interactors.UpdateListSort
import com.gnoemes.shimori.domain.interactors.tracks.AddTrackProgress
import com.gnoemes.shimori.domain.observers.ObserveListPage
import com.gnoemes.shimori.domain.observers.ObserveListSort
import com.gnoemes.shimori.domain.observers.ObserveTracksExist
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.screens.SettingsScreen
import com.gnoemes.shimori.screens.TrackEditScreen
import com.gnoemes.shimori.screens.TracksMenuScreen
import com.gnoemes.shimori.screens.TracksScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TracksScreen::class, UiScope::class)
class TracksPresenter(
    @Assisted private val navigator: Navigator,
    private val prefs: ShimoriPreferences,
    private val listsState: ListsStateBus,
    private val observeItems: Lazy<ObserveListPage>,
    private val observeSort: Lazy<ObserveListSort>,
    private val observeTracksExist: Lazy<ObserveTracksExist>,
    private val addTrackProgress: Lazy<AddTrackProgress>,
    private val changeSort: Lazy<UpdateListSort>,
) : Presenter<TracksUiState> {

    @Composable
    @ComposableTarget("presenter")
    override fun present(): TracksUiState {
        val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
        val isCompact by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isCompact() } }
        val isMedium by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isMedium() } }
        val isExpanded by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isExpanded() } }

        val uiMessageManager = remember { UiMessageManager() }

        val type by listsState.type.observe.collectAsRetainedState(TrackTargetType.valueOf(prefs.preferredListType))
        val status by listsState.page.observe.collectAsRetainedState(TrackStatus.valueOf(prefs.preferredListStatus))
        val sort by observeSort.value.flow.collectAsRetainedState(ListSort.defaultForType(type))

        val tracksExist by observeTracksExist.value.flow.collectAsRetainedState(false)
        val firstSyncLoading by listsState.tracksLoading.observe.collectAsState(false)

        val sortOptions by remember(type) {
            derivedStateOf {
                ListSortOption.priorityForType(type)
            }
        }

        val retainedPagingInteractor = rememberRetained(type, status, sort) { observeItems.value }
        //remember flow too. Presenter recompositions causes unwanted list update
        val itemsFlow = remember(type, status, sort) {
            retainedPagingInteractor.flow
                .filterIsInstance<PagingData<TitleWithTrackEntity>>()
        }
        val items = itemsFlow
            .rememberRetainedCachedPagingFlow()
            .collectAsLazyPagingItems()

        val scope = rememberCoroutineScope()
        val overlayHost = LocalOverlayHost.current

        val message by uiMessageManager.message.collectAsState(null)

        val isMenuButtonVisible = (isCompact || isMedium) && tracksExist
        prefs.nestedScaffoldContainsFab = isMenuButtonVisible

        LaunchedEffect(type, status, sort) {
            retainedPagingInteractor(
                ObserveListPage.Params(
                    type = type,
                    status = status,
                    sort = sort,
                    pagingConfig = PAGING_CONFIG
                )
            )
        }

        LaunchedEffect(type) {
            observeSort.value(ObserveListSort.Params(type))
        }

        LaunchedEffect(Unit) {
            observeTracksExist.value(Unit)
        }

        val eventSink: CoroutineScope.(TracksUiEvent) -> Unit = { event ->
            when (event) {
                is TracksUiEvent.OpenSettings -> navigator.goTo(SettingsScreen)

                is TracksUiEvent.AddOneToProgress -> launchOrThrow {
                    addTrackProgress.value(AddTrackProgress.Params(+1, event.track))
                }

                is TracksUiEvent.ChangeSort -> launchOrThrow {
                    changeSort.value(UpdateListSort.Params(event.sort))
                }

                is TracksUiEvent.OpenDetails -> launchOrThrow {

                }

                is TracksUiEvent.OpenEdit -> {
                    val screen = TrackEditScreen(
                        event.targetId,
                        event.targetType,
                        predefinedStatus = event.predefinedStatus
                    )

                    if (isCompact) navigator.goTo(screen)
                    else scope.launchOrThrow {
                        overlayHost.showInSideSheet(screen)
                    }
                }

                is TracksUiEvent.ClearMessage -> launchOrThrow {
                    uiMessageManager.clearMessage(event.id)
                }

                is TracksUiEvent.ActionMessage -> launchOrThrow {
                    val actionMessage = uiMessageManager.get(event.id)
                    uiMessageManager.clearMessage(event.id)
                }

                TracksUiEvent.OpenMenu -> {
                    val screen = TracksMenuScreen
                    when {
                        isCompact -> scope.launchOrThrow {
                            overlayHost.showInBottomSheet(screen)
                        }

                        isMedium -> scope.launchOrThrow {
                            overlayHost.showInSideSheet(screen)
                        }
                    }
                }
            }
        }

        return TracksUiState(
            type = type,
            status = status,
            isMenuButtonVisible = isMenuButtonVisible,
            isMenuVisible = isExpanded && tracksExist,
            sort = sort,
            sortOptions = sortOptions,
            items = items,
            firstSyncLoading = firstSyncLoading,
            uiMessage = message,
            eventSink = wrapEventSink(eventSink)
        )
    }


    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 60,
            initialLoadSize = 60,
        )
    }
}