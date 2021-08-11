package com.gnoemes.shimori.lists.page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.observers.ObservePagedAnimeRates
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.ListsPage
import com.gnoemes.shimori.model.rate.RateSort
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class ListPageViewModel @AssistedInject constructor(
    @Assisted internal val page: ListsPage,
    private val observeAnimeRates: ObservePagedAnimeRates,
    private val observeRateSort: ObserveRateSort,
    private val stateManager: ListsStateManager,
    private val getRandomTitleWithStatus: GetRandomTitleWithStatus
) : ViewModel() {

    val list get() = observeAnimeRates.observe()

    init {
        viewModelScope.launch {
            combine(
                    stateManager.currentType,
                    observeRateSort.observe().distinctUntilChanged()
            ) { type, sort ->
                ObservePagedAnimeRates.Params(
                        type = type,
                        page = page,
                        sort = sort ?: RateSort.defaultForType(type),
                        pagingConfig = PAGING_CONFIG
                )
            }
                .collect { observeAnimeRates(it) }
        }

        viewModelScope.launch {
            stateManager.currentType.collect {
                observeRateSort(ObserveRateSort.Params(it))
            }
        }

        viewModelScope.launch {
            stateManager.openRandomTitle
                .filter { stateManager.currentPage.value == page }
                .collect { openRandomTitle() }
        }
    }

    private fun openRandomTitle() {
        viewModelScope.launch {
            getRandomTitleWithStatus(
                    GetRandomTitleWithStatus.Params(
                            type = stateManager.currentType.value,
                            status = page.status
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
            page: ListsPage
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(page) as T
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
        fun create(page: ListsPage): ListPageViewModel
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