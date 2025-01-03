package com.gnoemes.shimori.sources.shikimori

import com.apollographql.apollo.ApolloClient
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.entities.Platform
import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.sources.shikimori.actions.ShikimoriRefreshTokenAction
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import java.util.concurrent.TimeUnit

expect interface ShikimoriPlatformComponent
interface ShikimoriComponent : ShikimoriPlatformComponent, ShikimoriAuthComponent {

    @ApplicationScope
    @Provides
    fun provideShikimoriGraphql(
        values: ShikimoriValues,
    ): ShikimoriApollo = ApolloClient.Builder()
        .serverUrl("${values.url}/api/graphql")
        .build()

    @ApplicationScope
    @Provides
    fun provideShikimoriKtor(
        applicationInfo: ApplicationInfo,
        values: ShikimoriValues,
        store: ShikimoriAuthStore,
        refreshTokenAction: ShikimoriRefreshTokenAction,
    ): ShikimoriKtor = HttpClient(OkHttp) {
        expectSuccess = true

        engine {
            config {
                followRedirects(true)
                connectionPool(ConnectionPool(10, 2000, TimeUnit.MILLISECONDS))
                dispatcher(
                    Dispatcher().apply { maxRequestsPerHost = 15 }
                )
            }
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = store.get()?.accessToken
                    val refreshToken = store.get()?.refreshToken
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else null
                }

                refreshTokens {
                    val oldTokens = this.oldTokens
                    if (oldTokens?.refreshToken == null) {
                        store.clear()
                        return@refreshTokens null
                    }

                    runBlocking {
                        val result = refreshTokenAction()

                        if (result == null) {
                            store.clear()
                            return@runBlocking null
                        }

                        BearerTokens(
                            result.accessToken,
                            result.refreshToken
                        )
                    }
                }
            }
        }

        install(Logging) {
            logger = when (applicationInfo.platform) {
                Platform.Android -> Logger.ANDROID
                else -> Logger.DEFAULT
            }
            level = if (applicationInfo.debug) LogLevel.BODY else LogLevel.NONE
        }

        install(UserAgent) {
            agent = values.userAgent
        }

        install(ContentNegotiation) {
            json(
                json = Json {
                    encodeDefaults = true
                    isLenient = true
                    allowStructuredMapKeys = true
                    prettyPrint = false
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

    }
}

typealias ShikimoriApollo = ApolloClient
typealias ShikimoriKtor = HttpClient