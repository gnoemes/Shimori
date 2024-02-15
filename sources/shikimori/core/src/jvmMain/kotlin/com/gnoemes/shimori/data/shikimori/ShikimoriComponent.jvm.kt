package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.entities.Platform
import com.gnoemes.shimori.base.inject.ApplicationScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import java.util.concurrent.TimeUnit

actual interface ShikimoriPlatformComponent {

    @OptIn(ExperimentalSerializationApi::class)
    @ApplicationScope
    @Provides
    fun provideShikimoriKtor(
        applicationInfo: ApplicationInfo,
        values: ShikimoriValues,
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

        //TODO
//        install(Auth) {
//            bearer {
//        loadTokens {
//            val accessToken = storage.shikimoriAccessToken
//            val refreshToken = storage.shikimoriRefreshToken
//            if (accessToken != null && refreshToken != null) {
//                BearerTokens(accessToken, refreshToken)
//            } else null
//        }
//
//        refreshTokens {
//            val oldTokens = this.oldTokens
//            if (oldTokens?.refreshToken == null) {
//                onAuthExpired()
//                return@refreshTokens null
//            }
//
//
//            val tokenResponse = auth.refreshToken(oldTokens.refreshToken) {
//                markAsRefreshTokenRequest()
//            }
//
//            if (tokenResponse == null || tokenResponse.isEmpty) {
//                onAuthExpired()
//                return@refreshTokens null
//            }
//
//            storage.shikimoriAccessToken = tokenResponse.accessToken
//            storage.shikimoriRefreshToken = tokenResponse.refreshToken
//
//            _state.emit(ShikimoriAuthState.LOGGED_IN)
//            BearerTokens(tokenResponse.accessToken!!, tokenResponse.refreshToken!!)
//            }
//        }

        install(Logging) {
            logger = when (applicationInfo.platform) {
                Platform.Android -> Logger.ANDROID
                else -> Logger.DEFAULT
            }
            level = if (applicationInfo.debug) LogLevel.BODY else LogLevel.NONE
        }

        install(UserAgent) {
            agent = applicationInfo.packageName
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