package com.gnoemes.shimori.lists.page

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gnoemes.shimori.common.utils.ShimoriUiMessageTextProvider
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveMangaWithRatePaged
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MangaPageViewModel @Inject constructor(
    private val pagingInteractor: ObserveMangaWithRatePaged,
    uiMessageTextProvider: ShimoriUiMessageTextProvider,
    listManager: ListsStateManager,
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

    override val listType = ListType.Anime
    val pagedList = pagingInteractor.flow.cachedIn(viewModelScope)

    override suspend fun loadPage(status: RateStatus, sort: RateSort) {
        pagingInteractor(
            ObserveMangaWithRatePaged.Params(
                status = status,
                sort = sort,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

}