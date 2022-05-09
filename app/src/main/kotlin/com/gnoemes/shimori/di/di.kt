package com.gnoemes.shimori.di

import android.app.Application
import android.content.Context
import com.gnoemes.shikimori.shikimoriModule
import com.gnoemes.shimori.BuildConfig
import com.gnoemes.shimori.R
import com.gnoemes.shimori.appinitializers.AppInitializers
import com.gnoemes.shimori.auth.authModule
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer
import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.base.core.entities.Platform
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.base.core.settings.ShimoriStorage
import com.gnoemes.shimori.base.shared.createLogger
import com.gnoemes.shimori.base.shared.extensions.defaultConfig
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.utils.*
import com.gnoemes.shimori.common_ui_imageloading.imageLoadingModule
import com.gnoemes.shimori.data.dataModule
import com.gnoemes.shimori.data.shared.databaseModule
import com.gnoemes.shimori.domain.domainModule
import com.gnoemes.shimori.lists.change.listsChangeModule
import com.gnoemes.shimori.lists.edit.listsEditModule
import com.gnoemes.shimori.lists.listsModule
import com.gnoemes.shimori.main.MainViewModel
import com.gnoemes.shimori.settings.ShimoriSettingsImpl
import com.gnoemes.shimori.settings.ShimoriStorageImpl
import com.gnoemes.shimori.settings.settingsModule
import com.gnoemes.shimori.shikimori.auth.ActivityShikimoriAuthManager
import com.gnoemes.shimori.shikimori.auth.ShikimoriAuthManager
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.kodein.di.*


val appModule = DI.Module("app") {
    importOnce(binds)
    importOnce(initializers)
    importOnce(networkModule)
    importOnce(imageLoadingModule)
    importOnce(databaseModule)
    importOnce(dataModule)
    importOnce(domainModule)

    importOnce(shikimoriModule)

    importOnce(features)

    bindSingleton(tag = KodeinTag.appName) { instance<Context>().getString(R.string.app_name) }
    bindSingleton(tag = KodeinTag.appVersion) { BuildConfig.VERSION_NAME }
    bindSingleton { createLogger() }

    bindSingleton {
        Platform(
            type = Platform.Type.Android,
            debug = BuildConfig.DEBUG,
            appVersion = instance(KodeinTag.appVersion),
            shikimoriURL = BuildConfig.ShikimoriBaseUrl,
            shikimoriClientId = BuildConfig.ShikimoriClientId,
            shikimoriSecretKey = BuildConfig.ShikimoriClientSecret,
            shikimoriUserAgent = instance(KodeinTag.appName),
            shikimoriRedirect = instance<Context>().let { context ->
                val scheme = context.getString(R.string.shikimori_redirect_scheme)
                val host = context.getString(R.string.shikimori_redirect_host)
                val path = context.getString(R.string.shikimori_redirect_path)

                "$scheme://$host$path"
            }
        )
    }

    bindSingleton {
        AppCoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }

    bindProvider { new(::ShimoriRateUtil) }
    bindProvider { new(::ShimoriDateTimeFormatter) }
}


private val binds = DI.Module(name = "appBinds") {
    bindSingleton<ShimoriSettings> { ShimoriSettingsImpl(instance()) }
    bindSingleton<ShimoriStorage> { ShimoriStorageImpl(instance()) }
    bindSingleton<ShikimoriAuthManager> { new(::ActivityShikimoriAuthManager) }
    bindSingleton<ShimoriTextProvider> { new(::ShimoriContextTextProvider) }
}

private val initializers = DI.Module(name = "initializers") {
    bindSet<AppInitializer<Application>>()
    bindProvider { new(::AppInitializers) }
}

private val networkModule = DI.Module(name = "network") {

    bindSingleton(KodeinTag.imageClient) { OkHttpClient.Builder().defaultConfig().build() }
    bindSingleton(KodeinTag.shikimori) {
        val platform = instance<Platform>()

        HttpClient(OkHttp) {
            engine {
                config {
                    defaultConfig(maxRequestPerHost = 5)
                }
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = if (platform.debug) LogLevel.BODY else LogLevel.NONE
            }

            install(UserAgent) {
                agent = platform.shikimoriUserAgent
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
}

private val features = DI.Module(name = "features") {
    bindViewModel { new(::MainViewModel) }

    importOnce(authModule)
    importOnce(listsModule)
    importOnce(listsChangeModule)
    importOnce(listsEditModule)
    importOnce(settingsModule)
}