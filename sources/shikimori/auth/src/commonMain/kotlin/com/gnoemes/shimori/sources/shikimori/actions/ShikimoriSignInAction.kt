package com.gnoemes.shimori.sources.shikimori.actions

import com.gnoemes.shimori.data.source.auth.toSimpleState
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.BasicAuthFlow
import com.gnoemes.shimori.source.SourceAuthState
import com.gnoemes.shimori.sources.shikimori.ShikimoriAuthStore
import com.gnoemes.shimori.sources.shikimori.ShikimoriId
import com.gnoemes.shimori.sources.shikimori.ShikimoriOpenIdClient
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import io.ktor.client.request.header
import me.tatarka.inject.annotations.Inject
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

@Inject
class ShikimoriSignInAction(
    private val values: ShikimoriValues,
    private val factory: Lazy<CodeAuthFlowFactory>,
    private val client: Lazy<ShikimoriOpenIdClient>,
    private val sourceId: Lazy<ShikimoriId>,
    private val store: Lazy<ShikimoriAuthStore>,
    private val logger: Logger
) : BasicAuthFlow {
    override suspend fun invoke(): SourceAuthState? {
        return try {
            factory.value
                .createAuthFlow(client.value)
                .getAccessToken(configureAuthUrl = {
                    parameters.append("prompt", "login")
                }) { header("User-Agent", values.userAgent) }
                .toSimpleState(sourceId.value)
                .also { state ->
                    logger.d(tag = "Shikimori") { "Authorization success. AccessToken: ${state.accessToken}" }
                    store.value.save(state)
                }
        } catch (e: Exception) {
            logger.e(e, tag = "Shikimori") { "Authorization failed" }
            null
        }
    }
}