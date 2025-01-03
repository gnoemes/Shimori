package com.gnoemes.shimori.data.source.auth

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.AuthSource
import com.gnoemes.shimori.source.SourceAuthState
import me.tatarka.inject.annotations.Inject

@Inject
@ApplicationScope
class AuthManager(
    private val sources: Set<AuthSource>,
    private val logger: Logger
) {

    private companion object {
        private const val TAG = "SourceAuthManager"
    }

    fun getAuthorizedSources() = sources.filter { it.isAuthorized }

    fun isAuthorized(sourceId: Long): Boolean {
        val source = sources.find { it.id == sourceId }
            ?: throw IllegalArgumentException("Source with id $sourceId not found")
        val isAuthorized = source.isAuthorized
        logger.d(tag = TAG) { "Source: ${source.name}. Authorized: $isAuthorized" }
        return isAuthorized
    }

    fun getAuthState(sourceId: Long): SourceAuthState? {
        val source = sources.find { it.id == sourceId }
            ?: throw IllegalArgumentException("Source with id $sourceId not found")
        val state = source.getState()
        logger.d(tag = TAG) { "Source: ${source.name}. Auth State: $state" }
        return state
    }

    suspend fun signIn(sourceId: Long): SourceAuthState? {
        val source = sources.find { it.id == sourceId }
            ?: throw IllegalArgumentException("Source with id $sourceId not found")
        logger.d(tag = TAG) { "Performing login for source: ${source.name}" }
        return source.signIn()
    }

    suspend fun signUp(sourceId: Long): SourceAuthState? {
        val source = sources.find { it.id == sourceId }
            ?: throw IllegalArgumentException("Source with id $sourceId not found")
        logger.d(tag = TAG) { "Performing registration for source: ${source.name}" }
        return source.signUp()
    }

    suspend fun refreshToken(sourceId: Long): SourceAuthState? {
        val source = sources.find { it.id == sourceId }
            ?: throw IllegalArgumentException("Source with id $sourceId not found")
        logger.d(tag = TAG) { "Performing token refresh for source: ${source.name}" }
        return source.refreshToken()
    }
}