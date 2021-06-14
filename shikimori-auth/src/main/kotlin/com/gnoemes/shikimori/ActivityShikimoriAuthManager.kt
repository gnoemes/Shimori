package com.gnoemes.shikimori

import android.content.Context
import android.content.Intent
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import javax.inject.Inject

internal class ActivityShikimoriAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val shikimoriManager: ShikimoriManager,
    private val requestProvider: Lazy<AuthorizationRequest>,
    private val clientAuth: Lazy<ClientAuthentication>
) : ShikimoriAuthManager {
    private val authService by lazy(LazyThreadSafetyMode.NONE) {
        AuthorizationService(context)
    }

    override fun buildLoginIntent(): Intent {
        return authService.getAuthorizationRequestIntent(requestProvider.get())
    }

    override fun onLoginResult(result: LoginShikimori.Result) {
        val (response, error) = result
        when {
            response != null -> {
                authService.performTokenRequest(
                        response.createTokenExchangeRequest(),
                        clientAuth.get()
                ) { tokenResponse, ex ->
                    val state = AuthState().apply {
                        update(tokenResponse, ex)
                    }
                    shikimoriManager.onNewAuthState(state)
                }
            }
        }
    }
}