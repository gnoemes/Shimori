package com.gnoemes.shimori.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(
    observeMyUserShort: ObserveMyUserShort
) : StateScreenModel<HomeScreenState>(HomeScreenState()) {

    init {
        coroutineScope.launch {
            observeMyUserShort.flow
                .collectLatest { user ->
                    mutableState.update {
                        it.copy(profileImage = user?.image)
                    }
                }
        }

        observeMyUserShort(Unit)
    }
}

data class HomeScreenState(
    val profileImage: ShimoriImage? = null,
)