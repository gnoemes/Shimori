package com.gnoemes.shimori.lists.sort

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.domain.interactors.UpdateListSort
import com.gnoemes.shimori.domain.observers.ObserveListSort
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ListSortScreenModel(
    private val listState: ListsStateBus,
    private val observeSort: ObserveListSort,
    private val updateSort: UpdateListSort,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<ListSortScreenState>(
    ListSortScreenState(),
    dispatchers,
) {

    init {
        ioCoroutineScope.launch {
            combine(
                listState.type.observe,
                observeSort.flow,
                listState.type.observe.map { ListSortOption.priorityForType(it) }
            ) { type, active, options ->
                ListSortScreenState(
                    listType = type,
                    activeSort = active ?: ListSort.defaultForType(type),
                    options = options
                )
            }.collectLatest { state ->
                mutableState.update { state }
            }
        }

        ioCoroutineScope.launch {
            listState.type.observe
                .map(ObserveListSort::Params)
                .collect { observeSort(it) }
        }
    }

    fun onSortChange(newSort: ListSortOption, isDescending: Boolean) {
        coroutineScope.launch {
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

@Immutable
internal data class ListSortScreenState(
    val listType: ListType = ListType.Anime,
    val activeSort: ListSort = ListSort.defaultForType(listType),
    val options: List<ListSortOption> = emptyList()
)