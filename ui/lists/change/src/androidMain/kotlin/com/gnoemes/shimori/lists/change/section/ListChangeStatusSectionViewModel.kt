package com.gnoemes.shimori.lists.change.section

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.list.ListsStateManager
import com.gnoemes.shimori.domain.observers.ObserveExistedStatuses
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ListChangeStatusSectionViewModel(
    savedStateHandle: SavedStateHandle,
    private val listsStateManager: ListsStateManager,
    observeStatuses: ObserveExistedStatuses
) : ViewModel() {
    private val sectionType = ListType.findOrDefault(savedStateHandle["type"] ?: 0)

    val state = combine(
        observeStatuses.flow,
        listsStateManager.type.observe,
        listsStateManager.page.observe
    ) { statuses, currentType, currentStatus ->
        ListChangeStatusSectionViewState(
            statuses = statuses,
            selectedStatus = if (currentType == sectionType) currentStatus else null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ListChangeStatusSectionViewState()
    )

    init {
        observeStatuses(ObserveExistedStatuses.Params(sectionType.rateType!!))
    }

    fun onStatusChanged(newStatus: RateStatus) {
        viewModelScope.launch {
            listsStateManager.type.update(sectionType)
            listsStateManager.page.update(newStatus)
        }
    }
}