package com.gnoemes.shikimori

import android.content.Context
import android.content.SharedPreferences
import androidx.core.net.toUri
import com.gnoemes.shikimori.auth.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.openid.appauth.*
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ShikimoriAuthModule {

    @Provides
    fun provideAuthState(manager: ShikimoriManager) = runBlocking {
        manager.state.first()
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("shikimori_auth", Context.MODE_PRIVATE)
    }

    @Provides
    @Named("shikimori-oauth-redirect")
    fun provideOAuthRedirect(@ApplicationContext context: Context): String {
        val scheme =
            context.getString(R.string.shikimori_redirect_scheme)
        val host = context.getString(R.string.shikimori_redirect_host)
        val path = context.getString(R.string.shikimori_redirect_path)

        return "$scheme://$host$path"
    }

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
        //TODO return friends
        setScopes("user_rates", "comments", "topics")
        setCodeVerifier(null)
    }.build()

    @Provides
    @Singleton
    fun provideClientAuth(@Named("shikimori-secret-key") clientSecret: String): ClientAuthentication =
        ClientSecretPost(clientSecret)
}