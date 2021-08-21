package com.gnoemes.shimori.lists.page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

internal abstract class BaseListViewModel(
    private val status: RateStatus,
    private val observeRateSort: ObserveRateSort,
    private val stateManager: ListsStateManager,
    private val getRandomTitleWithStatus: GetRandomTitleWithStatus,
    private val togglePin: ToggleListPin
) : ViewModel(){
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
        fun provideFactory(
            assistedFactory: AnimeListPageViewModel.Factory,
            status: RateStatus
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(status) as T
            }
        }

        fun provideFactory(
            assistedFactory: MangaListPageViewModel.Factory,
            status: RateStatus
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(status) as T
            }
        }

        val PAGING_CONFIG = PagingConfig(
                pageSize = 50,
                initialLoadSize = 50,
                prefetchDistance = 20
        )
    }


    @Module
    @InstallIn(ActivityRetainedComponent::class)
    interface AssistedInjectModule

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface ViewModelFactoryProvider {
        fun animePageFactory(): AnimeListPageViewModel.Factory
        fun mangaPageFactory(): MangaListPageViewModel.Factory
    }
}