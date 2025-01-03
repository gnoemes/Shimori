package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.logging.api.Logger
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.types.CodeChallengeMethod

expect interface ShikimoriAuthPlatformComponent

interface ShikimoriAuthComponent : ShikimoriAuthPlatformComponent, ShikimoriValuesComponent {

    @Provides
    @ApplicationScope
    fun provideShikimoriOAuthClient(
        values: ShikimoriValues,
        logger: Logger,
    ): ShikimoriOpenIdClient = OpenIdConnectClient(
        discoveryUri = null,
    ) {
        endpoints {
            tokenEndpoint = values.tokenUrl
            authorizationEndpoint = values.oAuthUrl
        }

        clientId = values.clientId
        clientSecret = values.secretKey

//        scope = AuthConstants.SCOPES.joinToString(separator = "+") { it }
        codeChallengeMethod = CodeChallengeMethod.S256
        redirectUri = values.oauthRedirect
    }

}

typealias ShikimoriAuthKtor = HttpClient
typealias ShikimoriOpenIdClient = OpenIdConnectClient