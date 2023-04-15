package com.gnoemes.shimori.di

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.gnoemes.shikimori.shikimoriModule
import com.gnoemes.shimori.BuildConfig
import com.gnoemes.shimori.R
import com.gnoemes.shimori.appinitializers.AppInitializers
import com.gnoemes.shimori.appinitializers.NavigationInitializer
import com.gnoemes.shimori.auth.AuthFeature
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer
import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.base.core.entities.Platform
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.base.core.settings.ShimoriStorage
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.shared.createLogger
import com.gnoemes.shimori.base.shared.extensions.defaultConfig
import com.gnoemes.shimori.common.ui.utils.ShimoriContextTextProvider
import com.gnoemes.shimori.common.ui.utils.ShimoriDateTimeFormatter
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.ShimoriTrackUtil
import com.gnoemes.shimori.common.ui.utils.bindViewModel
import com.gnoemes.shimori.common_ui_imageloading.imageLoadingModule
import com.gnoemes.shimori.data.dataModule
import com.gnoemes.shimori.data.shared.databaseModule
import com.gnoemes.shimori.domain.domainModule
import com.gnoemes.shimori.home.HomeFeature
import com.gnoemes.shimori.lists.ListsFeature
import com.gnoemes.shimori.lists.edit.TrackEditFeature
import com.gnoemes.shimori.lists.menu.ListMenuFeature
import com.gnoemes.shimori.main.MainViewModel
import com.gnoemes.shimori.settings.SettingsFeature
import com.gnoemes.shimori.settings.ShimoriSettingsImpl
import com.gnoemes.shimori.settings.ShimoriStorageImpl
import com.gnoemes.shimori.shikimori.auth.ActivityShikimoriAuthManager
import com.gnoemes.shimori.shikimori.auth.ShikimoriAuthManager
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.TrackSource
import com.gnoemes.shimori.tasks.tasksModule
import com.gnoemes.shimori.title.TitleDetailsFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSet
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.provider


val appModule = DI.Module("app") {
    importOnce(binds)
    importOnce(initializers)
    importOnce(networkModule)
    importOnce(imageLoadingModule)
    importOnce(databaseModule)
    importOnce(dataModule)
    importOnce(domainModule)
    importOnce(tasksModule)

    importOnce(catalogSources)
    importOnce(shikimoriModule)

    importOnce(featuresUi)

    bindSingleton(tag = KodeinTag.appName) { instance<Context>().getString(R.string.app_name) }
    bindSingleton(tag = KodeinTag.userAgent) { instance<Context>().getString(R.string.user_agent) }
    bindSingleton(tag = KodeinTag.appVersion) { BuildConfig.VERSION_NAME }

    //TODO remove with BuildConfig migration to multiplatform
    bindSingleton(tag = KodeinTag.Shikimori.url) { BuildConfig.ShikimoriBaseUrl }
    bindSingleton(tag = KodeinTag.Shikimori.clientId) { BuildConfig.ShikimoriClientId }
    bindSingleton(tag = KodeinTag.Shikimori.clientSecret) { BuildConfig.ShikimoriClientSecret }
    bindSingleton(tag = KodeinTag.Shikimori.oAuthRedirect) {
        instance<Context>().let { context ->
            val scheme = context.getString(R.string.shikimori_redirect_scheme)
            val host = context.getString(R.string.shikimori_redirect_host)
            val path = context.getString(R.string.shikimori_redirect_path)

            "$scheme://$host$path"
        }
    }

    bindSingleton { createLogger() }

    bindSingleton {
        Platform(
            type = Platform.Type.Android,
            debug = BuildConfig.DEBUG,
            appVersion = instance(KodeinTag.appVersion),
        )
    }

    bindSingleton {
        AppCoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }

    bindSingleton { WorkManager.getInstance(instance()) }

    bindProvider { new(::ShimoriTrackUtil) }
    bindProvider { new(::ShimoriDateTimeFormatter) }
}


private val binds = DI.Module(name = "appBinds") {
    bindSingleton<ShimoriSettings> { ShimoriSettingsImpl(instance()) }
    bindSingleton<ShimoriStorage> { ShimoriStorageImpl(instance()) }
    bindSingleton<ShikimoriAuthManager> {
        ActivityShikimoriAuthManager(
            instance(),
            instance(KodeinTag.Shikimori.tag),
            instance()
        )
    }
    bindSingleton<ShimoriTextProvider> { new(::ShimoriContextTextProvider) }
}

private val initializers = DI.Module(name = "initializers") {
    bindSet<AppInitializer<Application>> {
        add { provider { NavigationInitializer(features) } }
    }
    bindProvider { new(::AppInitializers) }
}

private val catalogSources = DI.Module(name = "catalogSources") {
    bindSet<CatalogueSource>()
    bindSet<TrackSource>()
}

private val networkModule = DI.Module(name = "network") {

    bindSingleton(KodeinTag.imageClient) { OkHttpClient.Builder().defaultConfig().build() }
    bindSingleton(KodeinTag.Shikimori.tag) {
        val platform = instance<Platform>()
        val storage = instance<ShimoriStorage>()
        val userAgent = instance<String>(KodeinTag.userAgent)

        HttpClient(OkHttp) {
            expectSuccess = true

            engine {
                config {
                    defaultConfig(maxRequestPerHost = 5)
                }
            }
            install(Auth)

            install(Logging) {
                logger = Logger.ANDROID
                level = if (platform.debug) LogLevel.BODY else LogLevel.NONE
            }

            install(UserAgent) {
                agent = userAgent
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

private val features = setOf(
    HomeFeature,
    AuthFeature,
    ListsFeature,
    ListMenuFeature,
    TrackEditFeature,
    SettingsFeature,
    TitleDetailsFeature
)


private val featuresUi = DI.Module(name = "features-ui") {
    bindViewModel { new(::MainViewModel) }

    features.forEach { importOnce(it.di) }
}