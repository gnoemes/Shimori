package com.gnoemes.shimori.shikimori.auth

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shimori.base.core.entities.Platform
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.openid.appauth.*

class ActivityShikimoriAuthManager constructor(
    private val context: Context,
    private val shikimori: Shikimori,
    private val platform: Platform,
    private val dispatchers: AppCoroutineDispatchers,
) : ShikimoriAuthManager {
    private val authService by lazy(LazyThreadSafetyMode.NONE) {
        AuthorizationService(context)
    }


    override fun buildLoginIntent(): Intent {
        return authService.getAuthorizationRequestIntent(
            AuthorizationRequest.Builder(
                AuthorizationServiceConfiguration(
                    ShikimoriConstants.AUTHORIZATION_ENDPOINT.toUri(),
                    ShikimoriConstants.TOKEN_ENDPOINT.toUri(),
                ),
                platform.shikimoriClientId,
                ResponseTypeValues.CODE,
                platform.shikimoriRedirect.toUri()
            ).defaultConfig().build()
        )
    }

    override fun buildRegisterIntent(): Intent {
        return authService.getAuthorizationRequestIntent(
            AuthorizationRequest.Builder(
                AuthorizationServiceConfiguration(
                    ShikimoriConstants.REGISTRATION_ENDPOINT.toUri(),
                    ShikimoriConstants.TOKEN_ENDPOINT.toUri(),
                ),
                platform.shikimoriClientId,
                ResponseTypeValues.CODE,
                platform.shikimoriRedirect.toUri()
            ).defaultConfig().build()
        )
    }

    override fun onLoginResult(result: LoginShikimori.Result) {
        val (response, error) = result
        when {
            response != null -> {
                authService.performTokenRequest(
                    response.createTokenExchangeRequest(),
                    ClientSecretPost(platform.shikimoriSecretKey)
                ) { tokenResponse, ex ->
                    val state = AuthState().apply {
                        update(tokenResponse, ex)
                    }
                    onNewAuthState(state)
                }
            }
        }
    }

    private fun onNewAuthState(newState: AuthState) {
        GlobalScope.launch(dispatchers.io) {
            val accessToken = newState.accessToken
            val refreshToken = newState.refreshToken

            if (accessToken != null && refreshToken != null) {
                shikimori.onAuthSuccess(accessToken, refreshToken)
            } else {
                shikimori.onAuthExpired()
            }
        }
    }

    private fun AuthorizationRequest.Builder.defaultConfig(): AuthorizationRequest.Builder {
        setAdditionalParameters(mapOf("User-agent" to "Shimori"))
        //TODO return friends
        setPrompt(AuthorizationRequest.Prompt.LOGIN)
        setScopes("user_rates", "comments", "topics")
        setCodeVerifier(null)
        return this
    }
}