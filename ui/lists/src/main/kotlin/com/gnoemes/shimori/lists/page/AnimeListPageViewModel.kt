package com.gnoemes.shimori.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveAnimeWithRatePaged
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
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

internal class AnimeListPageViewModel @AssistedInject constructor(
    @Assisted internal val status: RateStatus,
    private val observeAnime: ObserveAnimeWithRatePaged,
    observeRateSort: ObserveRateSort,
    stateManager: ListsStateManager,
    getRandomTitleWithStatus: GetRandomTitleWithStatus,
    togglePin: ToggleListPin
) : BaseListPageViewModel(status, observeRateSort, stateManager, getRandomTitleWithStatus, togglePin) {

    val list get() = observeAnime.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            combine(
                    stateManager.type.observe.filter { it != ListType.Pinned }
                        .distinctUntilChanged(),
                    observeRateSort.flow
            ) { type, sort ->
                ObserveAnimeWithRatePaged.Params(
                        status = status,
                        sort = sort ?: RateSort.defaultForType(type),
                        pagingConfig = PAGING_CONFIG
                )
            }
                .collect { observeAnime(it) }
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
    }

    @Module
    @InstallIn(ActivityRetainedComponent::class)
    interface AssistedInjectModule

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface ViewModelFactoryProvider {
        fun animePageFactory(): Factory
    }

    @AssistedFactory
    interface Factory {
        fun create(status: RateStatus): AnimeListPageViewModel
    }

}