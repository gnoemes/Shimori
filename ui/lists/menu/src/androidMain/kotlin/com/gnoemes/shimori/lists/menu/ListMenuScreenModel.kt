package com.gnoemes.shimori.lists.menu

import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.list.ListsStateBus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ListMenuScreenModel(
    private val listsStateBus: ListsStateBus,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<ListType>(ListType.Anime, dispatchers) {

    init {
        ioCoroutineScope.launch {
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

