package com.gnoemes.shimori.common.imageloading

import android.app.Application
import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.util.CoilUtils
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Inject

class CoilAppInitializer @OptIn(ExperimentalCoilApi::class)
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val imageInterceptor: ShimoriImageInterceptor,
) : AppInitializer {

    override fun init(app: Application) {
        val coilOkHttpClient = okHttpClient.newBuilder()
            .cache(CoilUtils.createDefaultCache(context))
            .build()

        Coil.setImageLoader {
            ImageLoader.Builder(app)
                .componentRegistry { add(imageInterceptor) }
                .okHttpClient(coilOkHttpClient)
                .build()
        }
    }
}