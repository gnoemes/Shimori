package com.gnoemes.shimori.shikimori.auth

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.entities.auth.TokenResponse
import com.gnoemes.shimori.base.core.entities.Platform
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.shikimori.auth.ShikimoriConstants.CODE_PATTERN
import com.gnoemes.shimori.shikimori.auth.ShikimoriConstants.OAUTH_ENDPOINT
import com.gnoemes.shimori.shikimori.auth.ShikimoriConstants.SIGN_IN_ENDPOINT
import com.gnoemes.shimori.shikimori.auth.ShikimoriConstants.SIGN_UP_ENDPOINT
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivityShikimoriAuthManager constructor(
    private val shikimori: Shikimori,
    private val platform: Platform,
    private val dispatchers: AppCoroutineDispatchers,
) : ShikimoriAuthManager {

    override fun buildLoginIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, platform.shikimori.oauthRedirect.toUri()).apply {
            val getAuthCodeUrl = Uri.parse(OAUTH_ENDPOINT)
                .buildUpon()
                .encodedQuery("scope=user_rates+comments+topics")
                .appendQueryParameter("client_id", platform.shikimori.clientId)
                .appendQueryParameter("redirect_uri", platform.shikimori.oauthRedirect)
                .appendQueryParameter("response_type", "code")
                .build()
                .toString()

            putExtra(KEY_START_URL, SIGN_IN_ENDPOINT)
            putExtra(KEY_TRIGGER_URL, platform.shikimori.url + "/")
            putExtra(KEY_TARGET_URL, getAuthCodeUrl)
            putExtra(KEY_AUTH_CODE_PATTERN, CODE_PATTERN)
        }
    }

    override fun buildRegisterIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, platform.shikimori.oauthRedirect.toUri()).apply {
            val getAuthCodeUrl = Uri.parse(OAUTH_ENDPOINT)
                .buildUpon()
                .encodedQuery("scope=user_rates+comments+topics")
                .appendQueryParameter("client_id", platform.shikimori.clientId)
                .appendQueryParameter("redirect_uri", platform.shikimori.oauthRedirect)
                .appendQueryParameter("response_type", "code")
                .build()
                .toString()

            putExtra(KEY_START_URL, SIGN_UP_ENDPOINT)
            putExtra(KEY_TRIGGER_URL, platform.shikimori.url + "/")
            putExtra(KEY_TARGET_URL, getAuthCodeUrl)
            putExtra(KEY_AUTH_CODE_PATTERN, CODE_PATTERN)
        }
    }

    override fun onLoginResult(result: LoginShikimori.Result) {
        GlobalScope.launch(dispatchers.io) {
            val (code, error) = result
            when {
                code != null -> onNewAuthState(shikimori.performTokenAuthorization(code))
                error != null -> onErrorAuthState(error)
            }
        }
    }

    private suspend fun onNewAuthState(tokens: TokenResponse?) {
        val accessToken = tokens?.accessToken
        val refreshToken = tokens?.refreshToken

        if (accessToken != null && refreshToken != null) {
            shikimori.onAuthSuccess(accessToken, refreshToken)
        } else {
            shikimori.onAuthExpired()
        }
    }

    private suspend fun onErrorAuthState(error: String) {
        shikimori.onAuthError(error)
    }
}