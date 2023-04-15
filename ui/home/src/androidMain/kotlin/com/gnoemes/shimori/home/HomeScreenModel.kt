package com.gnoemes.shimori.home

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeScreenModel(
    observeMyUserShort: ObserveMyUserShort,
    dispatchers : AppCoroutineDispatchers,
) : StateScreenModel<HomeScreenState>(HomeScreenState(), dispatchers) {

    init {
        ioCoroutineScope.launch {
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

internal data class HomeScreenState(
    val profileImage: ShimoriImage? = null,
)