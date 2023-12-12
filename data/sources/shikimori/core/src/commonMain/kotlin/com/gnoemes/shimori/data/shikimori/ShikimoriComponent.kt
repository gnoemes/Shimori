package com.gnoemes.shimori.data.shikimori

import com.apollographql.apollo3.ApolloClient
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.app.SourceValues
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides

expect interface ShikimoriPlatformComponent
interface ShikimoriComponent : ShikimoriPlatformComponent {
    @ApplicationScope
    @Provides
    fun provideShikimoriValues(
        info: ApplicationInfo
    ): ShikimoriValues {
        val baseUrl = BuildConfig.ShikimoriBaseUrl

        return SourceValues(
            url = baseUrl,
            clientId = BuildConfig.ShikimoriClientId,
            secretKey = BuildConfig.ShikimoriClientSecret,
            oauthRedirect = "${info.packageName}://auth/shikimori",
            signInUrl = "$baseUrl/users/sign_in",
            signUpUrl = "$baseUrl/users/sign_up",
            oAuthUrl = "$baseUrl/oauth/authorize",
        )
    }

    @ApplicationScope
    @Provides
    fun provideShikimoriGraphql(
        values: ShikimoriValues,
    ): ShikimoriApollo = ApolloClient.Builder()
        .serverUrl("${values.url}/api/graphql")
        .build()
}

typealias ShikimoriValues = SourceValues
typealias ShikimoriApollo = ApolloClient
typealias ShikimoriKtor = HttpClient