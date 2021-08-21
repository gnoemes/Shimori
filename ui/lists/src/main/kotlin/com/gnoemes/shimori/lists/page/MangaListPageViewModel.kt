package com.gnoemes.shimori.lists.page

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveMangaWithRatePaged
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

internal class MangaListPageViewModel @AssistedInject constructor(
    @Assisted internal val status: RateStatus,
    private val observeManga: ObserveMangaWithRatePaged,
    observeRateSort: ObserveRateSort,
    stateManager: ListsStateManager,
    getRandomTitleWithStatus: GetRandomTitleWithStatus,
    togglePin: ToggleListPin
) : BaseListViewModel(status, observeRateSort, stateManager, getRandomTitleWithStatus, togglePin) {

    val list get() = observeManga.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            combine(
                    stateManager.type.observe.filter { it != ListType.Pinned }
                        .distinctUntilChanged(),
                    observeRateSort.flow
            ) { type, sort ->
                ObserveMangaWithRatePaged.Params(
                        status = status,
                        sort = sort ?: RateSort.defaultForType(type),
                        pagingConfig = PAGING_CONFIG
                )
            }
                .collect { observeManga(it) }
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(status: RateStatus): MangaListPageViewModel
    }


}