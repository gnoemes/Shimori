package com.gnoemes.shimori.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.data.list.ListsUiEvents
import com.gnoemes.shimori.data.paging.PagingConfig
import com.gnoemes.shimori.data.paging.PagingData
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.ToggleTitlePin
import com.gnoemes.shimori.domain.observers.ObserveListPage
import com.gnoemes.shimori.domain.observers.ObserveListSort
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListPageViewModel(
    private val stateBus: ListsStateBus,
    private val observeListPage: ObserveListPage,
    private val observeListSort: ObserveListSort,
    private val togglePin: ToggleTitlePin,
    private val updateTrack: CreateOrUpdateTrack,
    observeMyUser: ObserveMyUserShort
) : ViewModel() {

    private val _uiEvents = MutableSharedFlow<UiEvents>()

    private val incrementerEvents = MutableStateFlow<TitleWithTrackEntity?>(null)

    val uiEvents: SharedFlow<UiEvents> get() = _uiEvents

    val state = combine(
        stateBus.type.observe,
        incrementerEvents,
        stateBus.tracksLoading.observe,
        ::ListPageViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListPageViewState.Empty
    )

    val items = observeListPage.flow
        .filterIsInstance<PagingData<TitleWithTrackEntity>>()
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
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

        viewModelScope.launch {
            stateBus.type.observe
                .map(ObserveListSort::Params)
                .collect(observeListSort::invoke)
        }

        observeMyUser(Unit)
    }

    fun togglePin(entity: TitleWithTrackEntity) {
        viewModelScope.launch {
            togglePin(ToggleTitlePin.Params(entity.type, entity.id)).collect { pinned ->
                stateBus.uiEvents(ListsUiEvents.PinStatusChanged(entity, pinned))
            }
        }
    }

    fun showIncrementer(title: TitleWithTrackEntity) {
        viewModelScope.launch {
            incrementerEvents.value = title
        }
    }

    fun updateProgressFromIncrementer(newProgress: Int) {
        viewModelScope.launch {
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