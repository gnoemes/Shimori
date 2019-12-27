package com.gnoemes.shimori.main

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.shikimori.ShikimoriManager
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.domain.interactors.GetMyUser
import com.gnoemes.shimori.domain.invoke
import com.gnoemes.shimori.domain.launchObserve
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainViewState,
    observeShikimoriAuth: ObserveShikimoriAuth,
    getMyUser: GetMyUser,
    private val shikimoriManager: ShikimoriManager
) : BaseViewModel<MainViewState>(initialState) {

    init {
        viewModelScope.launchObserve(observeShikimoriAuth) { flow ->
            flow.distinctUntilChanged().onEach {
                    if (it == ShikimoriAuthState.LOGGED_IN) {
                        getMyUser()
                    }
                }
        }

        observeShikimoriAuth()
    }

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