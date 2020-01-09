package com.gnoemes.shimori.rates

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

internal class RateViewModel @AssistedInject constructor(
    @Assisted initialState: RateViewState
) : BaseViewModel<RateViewState>(initialState) {

    init {
        setState {
            copy(
                    categories = RateStatus
                        .values()
                        .map { RateCategory(RateTargetType.ANIME, it, it.priority) },
                    selectedStatus = RateStatus.DROPPED
            )
        }
    }

    fun submitAction(action: RateAction) {
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