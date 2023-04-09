package com.gnoemes.shimori.lists.change

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.list.ListsStateBus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ListMenuScreenModel(
    private val listsStateBus: ListsStateBus,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<ListType>(ListType.Anime) {

    init {
        coroutineScope.launch(dispatchers.io) {
            listsStateBus.type
                .observe
                .collectLatest { type ->
                    mutableState.update { type }
                }
        }
    }

    fun openRandomTitle() {
        coroutineScope.launch {
            listsStateBus.openRandomTitleEvent(Unit)
        }
    }

    fun openPinned() {
        coroutineScope.launch {
            listsStateBus.type(ListType.Pinned)
        }
    }
}

