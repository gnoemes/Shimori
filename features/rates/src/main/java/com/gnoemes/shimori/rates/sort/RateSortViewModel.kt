package com.gnoemes.shimori.rates.sort

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.model.app.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

internal class RateSortViewModel @AssistedInject constructor(
    @Assisted initialState: RateSortViewState,
    private val updateRateSort: UpdateRateSort
) : BaseViewModel<RateSortViewState>(initialState) {

    fun updateRateSort(sortOption: RateSortOption) {
        withState { state ->
            updateRateSort(UpdateRateSort.Params(
                    state.type,
                    state.sort.status ?: RateStatus.WATCHING,
                    sortOption,
                    state.sort.isDescending
            ))
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: RateSortViewState): RateSortViewModel
    }

    companion object : MvRxViewModelFactory<RateSortViewModel, RateSortViewState> {
        override fun create(viewModelContext: ViewModelContext, state: RateSortViewState): RateSortViewModel? {
            val f: RateSortDialogFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return f.viewModelFactory.create(state)
        }

        override fun initialState(viewModelContext: ViewModelContext): RateSortViewState? {
            val f: RateSortDialogFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            val args = f.requireArguments()

            return RateSortViewState(
                    type = args.getSerializable("target") as RateTargetType,
                    sort = args.getSerializable("sort") as RateSort
            )
        }
    }
}