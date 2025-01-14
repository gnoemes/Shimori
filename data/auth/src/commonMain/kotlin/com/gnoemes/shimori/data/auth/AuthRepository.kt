package com.gnoemes.shimori.data.auth

import com.gnoemes.shimori.base.inject.ApplicationCoroutineScope
import com.gnoemes.shimori.data.source.auth.AuthExpirationEvents
import com.gnoemes.shimori.data.source.auth.AuthManager
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.SourceAuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class AuthRepository(
    scope: ApplicationCoroutineScope,
    private val expirations: AuthExpirationEvents,
    private val authManager: AuthManager,
    private val logger: Logger
) {
    private val _state = MutableStateFlow(getPersistedStates())
    val state: StateFlow<MutableMap<Long, AuthState>> = _state.asStateFlow()

    init {
        scope.launch {
            expirations.events.collect { sourceId ->
                updateAuthState(sourceId, null)
            }
        }
    }

    fun observeAuthSources() = authManager.observeSources()

    suspend fun signIn(sourceId: Long) {
        logger.d(tag = "[AuthRepository]") { "Requested login. Source: $sourceId" }
        authManager.signIn(sourceId).also {
            updateAuthState(sourceId, it)
        }
    }

    suspend fun signUp(sourceId: Long) {
        logger.d(tag = "[AuthRepository]") { "Requested registration. Source: $sourceId" }
        authManager.signUp(sourceId).also {
            updateAuthState(sourceId, it)
        }
    }

    private fun getPersistedStates(): HashMap<Long, AuthState> {
        return HashMap(
            authManager.getAuthorizedSources().associate { it.id to AuthState.LOGGED_IN }
        )
    }

    private fun updateAuthState(sourceId: Long, state: SourceAuthState?) {
        logger.d(tag = "[AuthRepository]") { "Updating AuthState $state of source: $sourceId" }

        if (state == null) {
            _state.update {
                HashMap(it + (sourceId to AuthState.LOGGED_OUT))
            }
        } else {
            _state.update {
                HashMap(
                    it + (sourceId to if (state.isAuthorized) AuthState.LOGGED_IN else AuthState.LOGGED_OUT)
                )
            }
        }

    }
}