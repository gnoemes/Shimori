package com.gnoemes.shimori.di

import android.app.Application
import android.content.Context
import com.gnoemes.shikimori.shikimoriModule
import com.gnoemes.shimori.BuildConfig
import com.gnoemes.shimori.R
import com.gnoemes.shimori.appinitializers.AppInitializers
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer
import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.base.core.entities.Platform
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.base.core.settings.ShimoriStorage
import com.gnoemes.shimori.base.shared.createLogger
import com.gnoemes.shimori.base.shared.extensions.defaultConfig
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.compose.utils.bindViewModel
import com.gnoemes.shimori.common_ui_imageloading.imageLoadingModule
import com.gnoemes.shimori.data.dataModule
import com.gnoemes.shimori.data.shared.databaseModule
import com.gnoemes.shimori.main.MainViewModel
import com.gnoemes.shimori.settings.ShimoriSettingsImpl
import com.gnoemes.shimori.settings.ShimoriStorageImpl
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.kodein.di.*


val appModule = DI.Module("app") {
    importOnce(binds)
    importOnce(initializers)
    importOnce(imageLoadingModule)
    importOnce(databaseModule)
    importOnce(dataModule)

    importOnce(shikimoriModule)

    importOnce(viewModels)

    bindSingleton(tag = KodeinTag.appName) { instance<Context>().getString(R.string.app_name) }
    bindSingleton { createLogger() }

    bindSingleton {
        Platform(
            type = Platform.Type.Android,
            debug = BuildConfig.DEBUG,
            appVersion = BuildConfig.VERSION_NAME,
            shikimoriURL = BuildConfig.ShikimoriBaseUrl,
            shikimoriClientId = BuildConfig.ShikimoriClientId,
            shikimoriSecretKey = BuildConfig.ShikimoriClientSecret,
            shikimoriUserAgent = instance(KodeinTag.appName)
        )
    }

    bindSingleton {
        AppCoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }

    bindProvider(KodeinTag.imageClient) { imageClient }
    bindSingleton(KodeinTag.shikimori) {
        OkHttp.create {
            config {
                defaultConfig()
            }
        }
    }
}


private val binds = DI.Module(name = "appBinds") {
    bindSingleton<ShimoriSettings> { ShimoriSettingsImpl(instance()) }
    bindSingleton<ShimoriStorage> { ShimoriStorageImpl(instance()) }
}

private val initializers = DI.Module(name = "initializers") {
    bindSet<AppInitializer<Application>>()
    bindProvider { new(::AppInitializers) }
}
private val imageClient by lazy {
    OkHttpClient.Builder()
        .defaultConfig()
        .build()
}

private val viewModels = DI.Module(name = "viewModels") {
    bindViewModel { MainViewModel(instance()) }
}