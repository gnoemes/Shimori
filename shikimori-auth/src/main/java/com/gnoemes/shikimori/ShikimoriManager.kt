package com.gnoemes.shikimori

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.base.AppNavigator
import com.gnoemes.shimori.base.di.ProcessLifetime
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ShikimoriManager @Inject constructor(
    @Named("auth") private val prefs: SharedPreferences,
    @Named("app") private val appNavigator: AppNavigator,
    private val dispatchers: AppCoroutineDispatchers,
    private val request: AuthorizationRequest,
    private val clientAuth: ClientAuthentication,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val shikimori: Lazy<Shikimori>
) {

    private val authState = ConflatedBroadcastChannel<AuthState>()

    private val _state = ConflatedBroadcastChannel(ShikimoriAuthState.LOGGED_OUT)

    val state: Flow<ShikimoriAuthState>
        get() = _state.asFlow()

    init {
        processScope.launch {
            authState.asFlow().collect {
                updateAuthState(it)

                shikimori.get().run {
                    accessToken = it.accessToken
                    refreshToken = it.refreshToken
                }

            }
        }

        processScope.launch {
            val state = withContext(dispatchers.io) {
                readAuthState()
            }
            authState.send(state)
        }
    }

    private suspend fun updateAuthState(authState: AuthState) {
        if (authState.isAuthorized) {
            _state.send(ShikimoriAuthState.LOGGED_IN)
        } else {
            _state.send(ShikimoriAuthState.LOGGED_OUT)
        }
    }

    fun startAuth(requestCode: Int, service: AuthorizationService, signUp: Boolean) {
        performAuth(requestCode, service)
    }

    private fun performAuth(code: Int, service: AuthorizationService) {
        service.performAuthorizationRequest(
                request,
                appNavigator.provideAuthHandleIntent(code)
        )
    }

    fun onAuthResponse(service: AuthorizationService, response: AuthorizationResponse) {
        service.performTokenRequest(
                response.createTokenExchangeRequest(),
                clientAuth,
                ::onToken
        )
    }

    fun onAuthException(error: AuthorizationException) {
        error.printStackTrace()
    }

    private fun onToken(response: TokenResponse?, e: AuthorizationException?) {
        val newState = AuthState().apply { update(response, e) }
        e?.printStackTrace()

        processScope.launch(dispatchers.main) {
            authState.send(newState)
        }

        processScope.launch(dispatchers.io) {
            persistAuthState(newState)
        }
    }

    private fun readAuthState(): AuthState {
        val stateJson = prefs.getString("stateJson", null)
        return when {
            stateJson != null -> AuthState.jsonDeserialize(stateJson)
            else -> AuthState()
        }
    }

    private fun persistAuthState(state: AuthState) {
        Log.i("AUTH", "persist ${state.jsonSerializeString()}")
        prefs.edit {
            putString("stateJson", state.jsonSerializeString())
        }
    }

}