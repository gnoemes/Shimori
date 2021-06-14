package com.gnoemes.shikimori

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import dagger.Lazy
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class ShikimoriManager @Inject constructor(
    @Named("auth") private val prefs: SharedPreferences,
    private val dispatchers: AppCoroutineDispatchers,
    private val shikimori: Lazy<Shikimori>
) {
    private val authState = MutableStateFlow<AuthState?>(EmptyAuthState)

    private val _state = MutableStateFlow(ShikimoriAuthState.LOGGED_OUT)

    val state: StateFlow<ShikimoriAuthState>
        get() = _state

    init {
        GlobalScope.launch(dispatchers.io) {
            authState.collect { state ->

                val newShikimoriAuthState = when (state?.isAuthorized) {
                    true -> ShikimoriAuthState.LOGGED_IN
                    else -> ShikimoriAuthState.LOGGED_OUT
                }

                _state.emit(newShikimoriAuthState)


                shikimori.get().run {
                    accessToken = state?.accessToken
                    refreshToken = state?.refreshToken
                }

            }
        }

        GlobalScope.launch(dispatchers.main) {
            val state = withContext(dispatchers.io) { readAuthState() }
            authState.value = state
        }

        GlobalScope.launch(dispatchers.io) {
            shikimori.get().authErrorState.collect { error ->
                if (error == true) {
                    onNewAuthState(EmptyAuthState)
                }
            }
        }
    }

    fun onNewAuthState(newState: AuthState) {
        GlobalScope.launch(dispatchers.main) {
            authState.value = newState
        }

        GlobalScope.launch(dispatchers.io) {
            persistAuthState(newState)
        }
    }

    private fun readAuthState(): AuthState {
        val stateJson = prefs.getString(PrefKey, null)
        return when {
            stateJson != null -> AuthState.jsonDeserialize(stateJson)
            else -> AuthState()
        }
    }

    private fun persistAuthState(state: AuthState?) {
        Log.i("AUTH", "persist ${state?.jsonSerializeString()}")
        prefs.edit {
            putString(PrefKey, state?.jsonSerializeString())
        }
    }


    companion object {
        private val EmptyAuthState = AuthState()
        private const val PrefKey = "stateJson"
    }
}