package com.gnoemes.shimori.lists.sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListSortViewModel @Inject constructor(
    private val listState: ListsStateManager,
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
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
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
                    isDescending = isDescending
                )
            ).collect()
        }
    }

}