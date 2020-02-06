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
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.launchObserve
import com.gnoemes.shimori.domain.observers.ObserveAnimeRates
import com.gnoemes.shimori.domain.observers.ObserveRates
import com.gnoemes.shimori.model.rate.RateStatus
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

internal class RateViewModel @AssistedInject constructor(
    @Assisted initialState: RateViewState,
    observeRates: ObserveRates,
    private val updateAnimeRates: UpdateAnimeRates,
    private val observeAnimeRates: ObserveAnimeRates,
    private val updateRates: UpdateRates,
    rateCategoryMapper: RateCategoryMapper
) : BaseViewModel<RateViewState>(initialState) {

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

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is RateAction.Refresh -> refresh(true)
                is RateAction.ChangeCategory -> onCategoryChanged(action)
            }

        }

        withState {
            observeRates(ObserveRates.Params(it.type))
            observeAnimeRates(ObserveAnimeRates.Params(it.selectedCategory
                ?: RateStatus.WATCHING, null))
        }

        refresh(false)
    }

    private fun onCategoryChanged(action: RateAction.ChangeCategory) {
        setState {
            copy(selectedCategory = action.newCategory)
        }
        observeAnimeRates(ObserveAnimeRates.Params(action.newCategory, null))
    }

    fun submitAction(action: RateAction) {
        viewModelScope.launch { pendingActions.send(action) }
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