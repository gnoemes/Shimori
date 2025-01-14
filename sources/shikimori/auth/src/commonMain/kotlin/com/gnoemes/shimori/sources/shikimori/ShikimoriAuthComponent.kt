package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.logging.api.Logger
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.types.CodeChallengeMethod
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

expect interface ShikimoriAuthPlatformComponent

@ContributesTo(AppScope::class)
interface ShikimoriAuthComponent : ShikimoriAuthPlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
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