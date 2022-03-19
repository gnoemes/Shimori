package com.gnoemes.shimori.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.UpdateAnimeRates
import com.gnoemes.shimori.domain.interactors.UpdateMangaRates
import com.gnoemes.shimori.domain.interactors.UpdateRanobeRates
import com.gnoemes.shimori.domain.observers.ObserveHasPinnedTitles
import com.gnoemes.shimori.domain.observers.ObserveHasRates
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListsViewModel @Inject constructor(
    private val stateManager: ListsStateManager,
    observeMyUserShort: ObserveMyUserShort,
    observePinned: ObserveHasPinnedTitles,
    observeRates: ObserveHasRates,
    private val updateAnimeRates: UpdateAnimeRates,
    private val updateMangaRates: UpdateMangaRates,
    private val updateRanobeRates: UpdateRanobeRates
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
        viewModelScope.launch {
            observeRates.flow.collect { if (it) observeCurrentPageUpdate() }
        }

        observeMyUserShort(Unit)
        observePinned(Unit)
        observeRates(Unit)
    }

    private fun observeCurrentPageUpdate() {
        viewModelScope.launch {
            combine(
                stateManager.type.observe,
                stateManager.page.observe,
            ) { type, page ->
                type to page
            }
                .distinctUntilChanged()
                .collect { (type, status) ->
                    when (type) {
                        ListType.Anime -> updateAnimeRates(status)
                        ListType.Manga -> updateMangaRates(status)
                        ListType.Ranobe -> updateRanobeRates(status)
                    }
                }
        }
    }

    private fun updateAnimeRates(status: RateStatus) {
        viewModelScope.launch {
            updateAnimeRates(UpdateAnimeRates.Params.OptionalUpdate.copy(status = status)).collect()
        }
    }

    private fun updateMangaRates(status: RateStatus) {
        viewModelScope.launch {
            updateMangaRates(UpdateMangaRates.Params.OptionalUpdate.copy(status = status)).collect()
        }
    }

    private fun updateRanobeRates(status: RateStatus) {
        viewModelScope.launch {
            updateRanobeRates(UpdateRanobeRates.Params.OptionalUpdate.copy(status = status)).collect()
        }
    }

}