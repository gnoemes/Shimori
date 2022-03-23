package com.gnoemes.shimori.lists.page

import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.common.utils.ShimoriUiMessageTextProvider
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObservePinnedTitles
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class PinPageViewModel @Inject constructor(
    private val observePinnedTitles: ObservePinnedTitles,
    listManager: ListsStateManager,
    uiMessageTextProvider: ShimoriUiMessageTextProvider,
    observeMy: ObserveMyUserShort,
    observeRateSort: ObserveRateSort,
    getRandomTitle: GetRandomTitleWithStatus,
    toggleListPin: ToggleListPin,
) : BasePageViewModel(
    listManager,
    uiMessageTextProvider,
    observeMy,
    observeRateSort,
    getRandomTitle,
    toggleListPin
) {

    val list = observePinnedTitles.flow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = emptyList()
    )

    override val listType: ListType = ListType.Pinned
    override suspend fun loadPage(status: RateStatus, sort: RateSort) {
        observePinnedTitles(ObservePinnedTitles.Params(sort))
    }

}