package com.gnoemes.shimori.common_ui_imageloading

import android.app.Application
import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.util.CoilUtils
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer
import okhttp3.OkHttpClient

@OptIn(ExperimentalCoilApi::class)
class CoilAppInitializer constructor(
    private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val imageInterceptor: ShimoriImageInterceptor,
) : AppInitializer<Application> {

    override fun init(context: Application) {
        val coilOkHttpClient = okHttpClient.newBuilder()
            .cache(CoilUtils.createDefaultCache(this.context))
            .build()

        Coil.setImageLoader {
            ImageLoader.Builder(context)
                .componentRegistry { add(imageInterceptor) }
                .okHttpClient(coilOkHttpClient)
                .build()
        }
    }
}