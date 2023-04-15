package com.gnoemes.shimori.lists.menu.section

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.domain.observers.ObserveExistedStatuses
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ListMenuStatusSectionScreenModel(
    typeInt: Int,
    private val listsStateBus: ListsStateBus,
    observeStatuses: ObserveExistedStatuses,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<ListMenuStatusSectionScreenState>(
    ListMenuStatusSectionScreenState(),
    dispatchers
) {
    private val sectionType = ListType.findOrDefault(typeInt)

    init {
        ioCoroutineScope.launch {
            combine(
                observeStatuses.flow,
                listsStateBus.type.observe,
                listsStateBus.page.observe
            ) { statuses, currentType, currentStatus ->
                ListMenuStatusSectionScreenState(
                    statuses = statuses,
                    selectedStatus = if (currentType == sectionType) currentStatus else null
                )
            }.collectLatest { state ->
                mutableState.update { state }
            }
        }

        observeStatuses(ObserveExistedStatuses.Params(sectionType.trackType!!))
    }

    fun onStatusChanged(newStatus: TrackStatus) {
        coroutineScope.launch {
            listsStateBus.type.update(sectionType)
            listsStateBus.page.update(newStatus)
        }
    }
}

@Immutable
internal data class ListMenuStatusSectionScreenState(
    val statuses: List<TrackStatus> = emptyList(),
    val selectedStatus: TrackStatus? = null
)