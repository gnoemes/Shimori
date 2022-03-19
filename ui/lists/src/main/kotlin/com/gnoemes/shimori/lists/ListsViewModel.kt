package com.gnoemes.shimori.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.observers.ObserveHasPinnedTitles
import com.gnoemes.shimori.domain.observers.ObserveHasRates
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.model.rate.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class ListsViewModel @Inject constructor(
    stateManager: ListsStateManager,
    observeMyUserShort: ObserveMyUserShort,
    observePinned: ObserveHasPinnedTitles,
    observeRates: ObserveHasRates,
) : ViewModel() {

    val state = combine(
        stateManager.type.observe,
        observeMyUserShort.flow,
        observePinned.flow,
        observeRates.flow,
        stateManager.ratesLoading.observe
    ) { type, user, hasPins, hasRates, isLoading ->
        ListsViewState(
            type = type,
            user = user,
            isEmpty = if (type == ListType.Pinned) !hasPins else !hasRates,
            hasRates = hasRates,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ListsViewState.Empty
    )

    init {
        observeMyUserShort(Unit)
        observePinned(Unit)
        observeRates(Unit)
    }

}