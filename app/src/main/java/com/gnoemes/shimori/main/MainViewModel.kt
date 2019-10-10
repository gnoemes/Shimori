package com.gnoemes.shimori.main

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainViewState
) : BaseViewModel<MainViewState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: MainViewState): MainViewModel
    }

    companion object : MvRxViewModelFactory<MainViewModel, MainViewState> {
        override fun create(viewModelContext: ViewModelContext, state: MainViewState): MainViewModel? {
            val activity: MainActivity = viewModelContext.activity()
            return activity.mainNavigationViewModelFactory.create(state)
        }
    }
}