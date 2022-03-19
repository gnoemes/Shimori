package com.gnoemes.shimori.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.gnoemes.shimori.common.api.UiMessageManager
import com.gnoemes.shimori.common.utils.MessageID
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal abstract class BasePageViewModel(
    private val listManager: ListsStateManager,
    observeMy: ObserveMyUserShort,
    private val observeRateSort: ObserveRateSort,
    private val getRandomTitleWithStatus: GetRandomTitleWithStatus
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    protected abstract val listType: ListType
    abstract suspend fun loadPage(status: RateStatus, sort: RateSort)

    val state = combine(
        listManager.type.observe,
        observeMy.flow,
        uiMessageManager.message,
        ::ListPageViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ListPageViewState.Empty
    )

    init {
        viewModelScope.launch {
            combine(
                listManager.page.observe,
                observeRateSort.flow,
            ) { status, sort ->
                status to sort
            }.collect { loadPage(it.first, it.second ?: RateSort.defaultForType(listType)) }
        }

        viewModelScope.launch {
            listManager.type.observe
                .map(ObserveRateSort::Params)
                .collect(observeRateSort::invoke)
        }

        observeMy(Unit)
    }

    fun showMessage(id: MessageID) {
        viewModelScope.launch {
//            uiMessageManager.emitMessage()
        }
    }

    fun onMessageShown(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 70,
            initialLoadSize = 70,
            prefetchDistance = 20
        )
    }
}