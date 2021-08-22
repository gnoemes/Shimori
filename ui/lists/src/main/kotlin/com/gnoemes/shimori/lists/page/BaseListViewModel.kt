package com.gnoemes.shimori.lists.page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

internal abstract class BaseListViewModel(
    private val status: RateStatus?,
    private val observeRateSort: ObserveRateSort,
    private val stateManager: ListsStateManager,
    private val getRandomTitleWithStatus: GetRandomTitleWithStatus,
    private val togglePin: ToggleListPin
) : ViewModel() {
    private val pendingActions = MutableSharedFlow<ListPageAction>()


    init {
        viewModelScope.launch {
            stateManager.type.observe.collect {
                observeRateSort(ObserveRateSort.Params(it))
            }
        }

        viewModelScope.launch {
            stateManager.openRandomTitleEvent.observe
                .filter { stateManager.page.value == status }
                .collect { openRandomTitle() }
        }

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is ListPageAction.TogglePin -> togglePin(action.id, action.targetType)
                }
            }
        }
    }

    fun submitAction(action: ListPageAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    private fun togglePin(shikimoriId: Long, targetType: RateTargetType) {
        viewModelScope.launch {
            togglePin(ToggleListPin.Params(targetType, shikimoriId)).collect()
        }
    }

    private fun openRandomTitle() {
        viewModelScope.launch {
            getRandomTitleWithStatus(
                    GetRandomTitleWithStatus.Params(
                            type = stateManager.type.value,
                            status = status
                    )
            ).collect {
                //TODO navigate to details
                Log.i("DEVE", "${(it?.entity as? Anime)?.name}")
            }
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
                pageSize = 50,
                initialLoadSize = 50,
                prefetchDistance = 20
        )
    }
}