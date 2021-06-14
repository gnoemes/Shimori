package com.gnoemes.shikimori.util

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject


class ShikimoriAuthenticator @Inject constructor(
    private val shikimori: Shikimori,
    private val dispatchers: AppCoroutineDispatchers
) : Authenticator {

    companion object {
        private const val ACCESS_TOKEN_HEADER = "Authorization"
    }

    @DelicateCoroutinesApi
    override fun authenticate(route: Route?, response: Response): Request? {
        if (Shikimori.BASE_HOST != response.request.url.host) {
            return null
        }

        if (responseCount(response) >= 2) {
            return null
        }

        val rfToken = shikimori.refreshToken
        if (rfToken.isNullOrBlank()) {
            return null
        }

        val refreshResponse = shikimori.refreshToken(rfToken)
        val token = refreshResponse.body()
        if (!refreshResponse.isSuccessful || token == null) {
            GlobalScope.launch(dispatchers.main) {
                shikimori.onAuthExpired()
            }
            return null
        }

        shikimori.run {
            accessToken = token.accessToken
            refreshToken = token.refreshToken
        }

        return response.request.newBuilder()
            .header(ACCESS_TOKEN_HEADER, "Bearer %s".format(token.accessToken))
            .build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var rs = response.priorResponse
        while (rs != null) {
            rs = rs.priorResponse
            result++
        }

        return result
    }
}