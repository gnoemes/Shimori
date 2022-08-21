package com.gnoemes.shimori.common_ui_imageloading

import android.app.Application
import coil.annotation.ExperimentalCoilApi
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer
import com.gnoemes.shimori.base.core.di.KodeinTag
import org.kodein.di.DI
import org.kodein.di.inSet
import org.kodein.di.instance
import org.kodein.di.provider

@OptIn(ExperimentalCoilApi::class)
actual val imageLoadingModule: DI.Module
    get() = DI.Module("imageLoading") {

        inSet<AppInitializer<Application>> {
            provider {
                CoilAppInitializer(
                    instance(),
                    instance(tag = KodeinTag.imageClient),
                    ShimoriImageInterceptor(),
                )
            }
        }
    }