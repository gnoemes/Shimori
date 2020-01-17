package com.gnoemes.shimori.rates

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.common.utils.ObservableLoadingCounter
import com.gnoemes.shimori.data_base.mappers.toLambda
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.launchObserve
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

        viewModelScope.launchObserve(observeRates) { flow ->
            flow.distinctUntilChanged()
                .execute(rateCategoryMapper.toLambda()) {
                    if (it is Success) {
                        val status = selectedCategory ?: RateStatus.WATCHING
                        copy(selectedCategory = status, categories = it())
                    } else this
                }
        }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is RateAction.Refresh -> refresh(true)
                is RateAction.ChangeCategory -> onCategoryChanged(action)
            }

        }

        withState {
            observeRates(ObserveRates.Params(it.type))
        }

        refresh(false)
    }

    private fun onCategoryChanged(action: RateAction.ChangeCategory) {
        setState {
            copy(selectedCategory = action.newCategory)
        }
    }

    fun submitAction(action: RateAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    private fun refresh(force: Boolean) {

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