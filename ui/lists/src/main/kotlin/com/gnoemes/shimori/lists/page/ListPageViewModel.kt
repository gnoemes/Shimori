package com.gnoemes.shimori.lists.page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObservePagedTitleRates
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListPageViewModel @AssistedInject constructor(
    @Assisted internal val status: RateStatus,
    private val observeTitleRates: ObservePagedTitleRates,
    private val observeRateSort: ObserveRateSort,
    private val stateManager: ListsStateManager,
    private val getRandomTitleWithStatus: GetRandomTitleWithStatus,
    private val togglePin: ToggleListPin
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ListPageAction>()

    val list get() = observeTitleRates.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            combine(
                    stateManager.currentType.filter { it != ListType.Pinned }.distinctUntilChanged(),
                    observeRateSort.flow
            ) { type, sort ->
                ObservePagedTitleRates.Params(
                        type = type.rateType!!,
                        status = status,
                        sort = sort ?: RateSort.defaultForType(type),
                        pagingConfig = PAGING_CONFIG
                )
            }
                .collect { observeTitleRates(it) }
        }

        viewModelScope.launch {
            stateManager.currentType.collect {
                observeRateSort(ObserveRateSort.Params(it))
            }
        }

        viewModelScope.launch {
            stateManager.openRandomTitle
                .filter { stateManager.currentPage.value == status }
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
                            status = status
                    )
            ).collect {
                //TODO navigate to details
                Log.i("DEVE", "${(it?.entity as? Anime)?.name}")
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            status: RateStatus
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(status) as T
            }
        }

        private val PAGING_CONFIG = PagingConfig(
                pageSize = 50,
                initialLoadSize = 50,
                prefetchDistance = 20
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(status: RateStatus): ListPageViewModel
    }

    @Module
    @InstallIn(ActivityRetainedComponent::class)
    interface AssistedInjectModule

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface ViewModelFactoryProvider {
        fun pageFactory(): Factory
    }
}