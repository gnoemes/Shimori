package com.gnoemes.shimori.lists.sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListSortViewModel(
    private val listState: ListsStateBus,
    private val observeSort: ObserveRateSort,
    private val updateSort: UpdateRateSort,
) : ViewModel() {

    val state = combine(
        listState.type.observe,
        observeSort.flow,
        listState.type.observe.map { RateSortOption.priorityForType(it) }
    ) { type, active, options ->
        ListSortViewState(
            listType = type,
            activeSort = active ?: RateSort.defaultForType(type),
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
                .map(ObserveRateSort::Params)
                .collect { observeSort(it) }
        }
    }

    fun onSortChange(newSort: RateSortOption, isDescending: Boolean) {
        viewModelScope.launch {
            updateSort(
                UpdateRateSort.Params(
                    type = state.value.listType,
                    sort = newSort,
                    descending = isDescending
                )
            ).collect()
        }
    }

}