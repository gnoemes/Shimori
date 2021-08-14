package com.gnoemes.shimori.lists.page.pinned

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.lists.page.ListPageAction
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListPinnedPageViewModel @Inject constructor(
    private val observeRateSort: ObserveRateSort,
    private val stateManager: ListsStateManager,
    private val getRandomTitleWithStatus: GetRandomTitleWithStatus,
    private val togglePin: ToggleListPin
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ListPageAction>()


    init {
        viewModelScope.launch {
            combine(
                    stateManager.currentType,
                    observeRateSort.observe().distinctUntilChanged()
            ) { type, sort ->

            }
        }

        viewModelScope.launch {
            stateManager.currentType.collect {
                observeRateSort(ObserveRateSort.Params(it))
            }
        }

        viewModelScope.launch {
            stateManager.openRandomTitle
                .filter { stateManager.currentType.value == ListType.Pinned }
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
                            type = stateManager.currentType.value,
                            status = null
                    )
            ).collect {
                //TODO navigate to details
                Log.i("DEVE", "${(it?.entity as? Anime)?.name}")
            }
        }
    }
}