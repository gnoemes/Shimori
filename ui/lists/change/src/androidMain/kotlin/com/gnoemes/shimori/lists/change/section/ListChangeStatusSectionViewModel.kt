package com.gnoemes.shimori.lists.change.section

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.domain.observers.ObserveExistedStatuses
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ListChangeStatusSectionViewModel(
    savedStateHandle: SavedStateHandle,
    private val listsStateBus: ListsStateBus,
    observeStatuses: ObserveExistedStatuses
) : ViewModel() {
    private val sectionType = ListType.findOrDefault(savedStateHandle["type"] ?: 0)

    val state = combine(
        observeStatuses.flow,
        listsStateBus.type.observe,
        listsStateBus.page.observe
    ) { statuses, currentType, currentStatus ->
        ListChangeStatusSectionViewState(
            statuses = statuses,
            selectedStatus = if (currentType == sectionType) currentStatus else null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListChangeStatusSectionViewState()
    )

    init {
        observeStatuses(ObserveExistedStatuses.Params(sectionType.rateType!!))
    }

    fun onStatusChanged(newStatus: RateStatus) {
        viewModelScope.launch {
            listsStateBus.type.update(sectionType)
            listsStateBus.page.update(newStatus)
        }
    }
}