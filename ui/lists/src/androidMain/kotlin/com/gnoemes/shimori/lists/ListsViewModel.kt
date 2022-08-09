package com.gnoemes.shimori.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.list.ListsStateManager
import com.gnoemes.shimori.domain.interactors.UpdateTitleRates
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObservePinsExist
import com.gnoemes.shimori.domain.observers.ObserveRatesExist
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListsViewModel(
    private val stateManager: ListsStateManager,
    private val updateTitleRates: UpdateTitleRates,
    observeRatesExist: ObserveRatesExist,
    observePinsExist: ObservePinsExist,
    observeMyUser: ObserveMyUserShort,
) : ViewModel() {

    val state = combine(
        stateManager.type.observe,
        observeMyUser.flow,
        observePinsExist.flow,
        observeRatesExist.flow,
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
        viewModelScope.launch {
            combine(
                stateManager.type.observe,
                stateManager.page.observe,
            ) { type, page ->
                val rateType = type.rateType ?: return@combine null
                rateType to page
            }
                .filterNotNull()
                .distinctUntilChanged()
                .collect(::updatePage)
        }

        observeMyUser(Unit)
        observePinsExist(Unit)
        observeRatesExist(Unit)
    }

    private fun updatePage(pair: Pair<RateTargetType, RateStatus>) {
        val (type, status) = pair
        viewModelScope.launch {
            updateTitleRates(
                UpdateTitleRates.Params.optionalUpdate(
                    type = type,
                    status = status
                )
            ).collect()
        }
    }
}