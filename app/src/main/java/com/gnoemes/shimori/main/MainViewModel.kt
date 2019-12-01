package com.gnoemes.shimori.main

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.shikimori.ShikimoriManager
import com.gnoemes.common.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainViewState,
    //TODO visibility
    val shikimoriManager: ShikimoriManager
) : BaseViewModel<MainViewState>(initialState) {

    fun onAuth(service: AuthorizationService) {
        shikimoriManager.startAuth(42, service, false)
    }

    fun onAuthResult(
        authService: AuthorizationService,
        response: AuthorizationResponse?,
        error: AuthorizationException?
    ) {
        when {
            response != null -> shikimoriManager.onAuthResponse(authService, response)
            error != null -> shikimoriManager.onAuthException(error)
        }
    }

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