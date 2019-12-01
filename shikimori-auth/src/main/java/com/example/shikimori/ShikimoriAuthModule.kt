package com.example.shikimori

import android.content.Context
import android.content.SharedPreferences
import androidx.core.net.toUri
import com.example.shikimori.auth.R
import dagger.Module
import dagger.Provides
import net.openid.appauth.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class ShikimoriAuthModule {

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("shikimori_auth", Context.MODE_PRIVATE)
    }

    @Provides
    @Named("shikimori-oauth-redirect")
    fun provideOAuthRedirect(context: Context) =
        context.getString(R.string.shikimori_redirect_scheme)

    @Singleton
    @Provides
    fun provideAuthConfig(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
                ShikimoriConstants.AUTHORIZATION_ENDPOINT.toUri(),
                ShikimoriConstants.TOKEN_ENDPOINT.toUri(),
                ShikimoriConstants.REGISTRATION_ENDPOINT.toUri()
        )
    }

    @Provides
    fun provideAuthRequest(
        authServiceConfig: AuthorizationServiceConfiguration,
        @Named("shikimori-client-id") clientId: String,
        @Named("shikimori-oauth-redirect") redirect: String
    ): AuthorizationRequest = AuthorizationRequest.Builder(
            authServiceConfig,
            clientId,
            ResponseTypeValues.CODE,
            redirect.toUri()
    ).apply {
        setAdditionalParameters(mapOf("User-agent" to "Shimori"))
        setScopes("user_rates", "comments", "topics", "friends")
        setCodeVerifier(null)
    }.build()

    @Provides
    @Singleton
    fun provideClientAuth(@Named("shikimori-secret-key") clientSecret: String): ClientAuthentication =
        ClientSecretPost(clientSecret)
}