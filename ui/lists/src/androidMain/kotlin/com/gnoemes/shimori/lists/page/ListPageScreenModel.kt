package com.gnoemes.shimori.lists.page

import androidx.compose.runtime.Immutable
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.data.list.ListsUiEvents
import com.gnoemes.shimori.data.paging.PagingConfig
import com.gnoemes.shimori.data.paging.PagingData
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.ToggleTitlePin
import com.gnoemes.shimori.domain.observers.ObserveListPage
import com.gnoemes.shimori.domain.observers.ObserveListSort
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ListPageScreenModel(
    private val stateBus: ListsStateBus,
    private val observeListPage: ObserveListPage,
    private val observeListSort: ObserveListSort,
    private val togglePin: ToggleTitlePin,
    private val updateTrack: CreateOrUpdateTrack,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<ListPageScreenState>(ListPageScreenState(), dispatchers) {
    private val _uiEvents = MutableSharedFlow<UiEvents>()

    private val incrementerEvents = MutableStateFlow<TitleWithTrackEntity?>(null)

    val uiEvents: SharedFlow<UiEvents> get() = _uiEvents

    val items = observeListPage.flow
        .filterIsInstance<PagingData<TitleWithTrackEntity>>()
        .cachedIn(ioCoroutineScope)

    init {
        ioCoroutineScope.launch {
            combine(
                stateBus.type.observe,
                stateBus.page.observe,
                incrementerEvents,
                ::ListPageScreenState
            ).collectLatest { state ->
                mutableState.update { state }
            }
        }

        ioCoroutineScope.launch {
            combine(
                stateBus.type.observe,
                stateBus.page.observe,
                observeListSort.flow,
            ) { type, status, sort ->
                ObserveListPage.Params(
                    type,
                    status,
                    sort ?: ListSort.defaultForType(type),
                    PAGING_CONFIG
                )
            }
                .distinctUntilChanged()
                .collect(observeListPage::invoke)
        }

        ioCoroutineScope.launch {
            stateBus.type.observe
                .map(ObserveListSort::Params)
                .collect(observeListSort::invoke)
        }
    }

    fun togglePin(entity: TitleWithTrackEntity) {
        coroutineScope.launch {
            togglePin(ToggleTitlePin.Params(entity.type, entity.id)).collect { pinned ->
                stateBus.uiEvents(ListsUiEvents.PinStatusChanged(entity, pinned))
            }
        }
    }

    fun showIncrementer(title: TitleWithTrackEntity) {
        coroutineScope.launch {
            incrementerEvents.value = title
        }
    }

    fun updateProgressFromIncrementer(newProgress: Int) {
        coroutineScope.launch {
            //get title from incrementer
            val title = incrementerEvents.value ?: return@launch
            //hide incrementer
            incrementerEvents.value = null

            val oldTrack = title.track ?: return@launch
            val newTrack = oldTrack.copy(progress = newProgress)

            if (oldTrack.progress == newProgress) return@launch

            //if progress is max, show edit track sheet and mark complete instead
            if (newProgress == title.entity.size) {
                _uiEvents.emit(UiEvents.EditTrack(title, markComplete = true))
                return@launch
            }

            updateTrack.invoke(
                CreateOrUpdateTrack.Params(newTrack)
            ).collect {
                if (it.isSuccess) {
                    stateBus.uiEvents(
                        ListsUiEvents.IncrementerProgress(
                            title,
                            oldTrack,
                            newProgress
                        )
                    )
                }
            }
        }
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
            prefetchDistance = 5
        )
    }
}

@Immutable
internal data class ListPageScreenState(
    val type: ListType = ListType.Anime,
    val status: TrackStatus = TrackStatus.WATCHING,
    val incrementerTitle: TitleWithTrackEntity? = null,
)

internal sealed class UiEvents {
    class EditTrack(val entity: TitleWithTrackEntity, val markComplete: Boolean) : UiEvents()
}