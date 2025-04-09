package com.gnoemes.shimori.source.shikimori.actions

import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.auth.BasicAuthAction
import com.gnoemes.shimori.source.model.SourceAuthState
import com.gnoemes.shimori.source.shikimori.ShikimoriId
import com.gnoemes.shimori.source.shikimori.ShikimoriOpenIdClient
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.auth.ShikimoriAuthStore
import com.gnoemes.shimori.source.shikimori.toSimpleState
import io.ktor.client.request.header
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriRefreshTokenAction(
    private val values: ShikimoriValues,
    private val client: Lazy<ShikimoriOpenIdClient>,
    private val sourceId: Lazy<ShikimoriId>,
    private val store: Lazy<ShikimoriAuthStore>,
    private val logger: Logger
) : BasicAuthAction {
    override suspend fun invoke(): SourceAuthState? {
        return try {
            val refreshToken = store.value.get()?.refreshToken

            if (refreshToken.isNullOrBlank()) {
                store.value.clear()
                return null
            }

            client.value
                .refreshToken(refreshToken) { header("User-Agent", values.userAgent) }
                .toSimpleState(sourceId.value)
                .also { state ->
                    logger.d(tag = "Shikimori") { "Token refresh success. AccessToken: ${state.accessToken}" }
                    store.value.save(state)
                }
        } catch (e: Exception) {
            logger.e(e, tag = "Shikimori") { "Token refresh failed" }
            store.value.clear()
            null
        }
    }
}