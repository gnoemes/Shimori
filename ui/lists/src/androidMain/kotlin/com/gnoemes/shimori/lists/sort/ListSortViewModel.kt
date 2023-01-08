package com.gnoemes.shimori.lists.sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.domain.interactors.UpdateListSort
import com.gnoemes.shimori.domain.observers.ObserveListSort
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListSortViewModel(
    private val listState: ListsStateBus,
    private val observeSort: ObserveListSort,
    private val updateSort: UpdateListSort,
) : ViewModel() {

    val state = combine(
        listState.type.observe,
        observeSort.flow,
        listState.type.observe.map { ListSortOption.priorityForType(it) }
    ) { type, active, options ->
        ListSortViewState(
            listType = type,
            activeSort = active ?: ListSort.defaultForType(type),
            options = options
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListSortViewState.Empty
    )

    init {
        viewModelScope.launch {
            listState.type.observe
                .map(ObserveListSort::Params)
                .collect { observeSort(it) }
        }
    }

    fun onSortChange(newSort: ListSortOption, isDescending: Boolean) {
        viewModelScope.launch {
            updateSort(
                UpdateListSort.Params(
                    type = state.value.listType,
                    sort = newSort,
                    descending = isDescending
                )
            ).collect()
        }
    }

}