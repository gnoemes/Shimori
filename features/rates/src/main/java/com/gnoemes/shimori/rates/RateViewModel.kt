package com.gnoemes.shimori.rates

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.common.utils.ObservableLoadingCounter
import com.gnoemes.common.utils.collectFrom
import com.gnoemes.shimori.data_base.mappers.toLambda
import com.gnoemes.shimori.domain.interactors.UpdateAnimeRates
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.launchObserve
import com.gnoemes.shimori.domain.observers.ObserveAnimeRates
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.domain.observers.ObserveRates
import com.gnoemes.shimori.model.app.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class RateViewModel @AssistedInject constructor(
    @Assisted initialState: RateViewState,
    observeRates: ObserveRates,
    private val updateAnimeRates: UpdateAnimeRates,
    private val observeAnimeRates: ObserveAnimeRates,
    private val observeRateSort: ObserveRateSort,
    private val updateRates: UpdateRates,
    private val updateRateSort: UpdateRateSort,
    rateCategoryMapper: RateCategoryMapper
) : BaseViewModel<RateViewState>(initialState) {

    private val searchQuery = ConflatedBroadcastChannel<String>()
    private val updateRatesCountLoadingState = ObservableLoadingCounter()
    private val updateRatesLoadingState = ObservableLoadingCounter()
    private val pendingActions = Channel<RateAction>(Channel.BUFFERED)

    init {
        viewModelScope.launch {
            updateRatesCountLoadingState.observable.collect { loading ->
                setState { copy(categoriesRefreshing = loading) }
            }
        }

        viewModelScope.launch {
            updateRatesLoadingState.observable.collect { loading ->
                setState { copy(isRefreshing = loading) }
            }
        }

        viewModelScope.launch {
            searchQuery.asFlow()
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val filter = if (query.isBlank()) null else query
                    setState { copy(query = filter) }
                }
        }

        viewModelScope.launchObserve(observeRates) { flow ->
            flow.distinctUntilChanged()
                .execute(rateCategoryMapper.toLambda()) {
                    if (it is Success) {
                        val status = selectedCategory ?: RateStatus.WATCHING
                        copy(selectedCategory = status, categories = it())
                    } else this
                }
        }

        viewModelScope.launchObserve(observeAnimeRates) { flow ->
            flow.distinctUntilChanged().execute { copy(rates = it) }
        }

        viewModelScope.launchObserve(observeRateSort) { flow ->
            flow.distinctUntilChanged().execute {
                if (it is Success) {
                    it()?.let { rateSort -> copy(sort = rateSort) } ?: this
                } else this
            }
        }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is RateAction.Refresh -> refresh(true)
                is RateAction.ChangeCategory -> onCategoryChanged(action)
                is RateAction.ChangeOrder -> onChangeOrder()
            }

        }

        withState {
            observeRates(ObserveRates.Params(it.type))
            val rateStatus = it.selectedCategory ?: RateStatus.WATCHING
            observeRateSort(ObserveRateSort.Params(it.type, rateStatus))
        }

        selectSubscribe(RateViewState::sort,  RateViewState::query) { sort, query ->
            withState { state ->
                val status = state.selectedCategory ?: RateStatus.WATCHING
                observeAnimeRates(ObserveAnimeRates.Params(status, sort, query))
            }
        }

        refresh(false)
    }

    private fun onChangeOrder() {
        withState {
            updateRateSort(UpdateRateSort.Params(
                    it.type,
                    it.selectedCategory!!,
                    it.sort.sortOption,
                    it.sort.isDescending.not())
            )
        }
    }

    private fun onCategoryChanged(action: RateAction.ChangeCategory) {
        setState {
            copy(
                    selectedCategory = action.newCategory,
                    sort = RateSort.default().copy(status = action.newCategory)
            )
        }
        withState {
            observeRateSort(ObserveRateSort.Params(it.type, action.newCategory))
        }
    }

    fun submitAction(action: RateAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun setSearchQuery(newText: String) {
        searchQuery.sendBlocking(newText)
    }

    private fun refresh(force: Boolean) = withState { state ->
        state.selectedCategory?.let { status ->
            updateAnimeRates(UpdateAnimeRates.Params(force, status)).also {
                viewModelScope.launch {
                    updateRatesLoadingState.collectFrom(it)
                }
            }
        }
        updateRates(Unit).also {
            viewModelScope.launch {
                updateRatesCountLoadingState.collectFrom(it)
            }
        }

    }


    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: RateViewState): RateViewModel
    }

    companion object : MvRxViewModelFactory<RateViewModel, RateViewState> {
        override fun create(viewModelContext: ViewModelContext, state: RateViewState): RateViewModel? {
            val fragment: RateFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}