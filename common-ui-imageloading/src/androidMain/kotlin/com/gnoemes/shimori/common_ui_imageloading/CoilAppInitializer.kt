package com.gnoemes.shimori.common_ui_imageloading

import android.app.Application
import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import coil.memory.MemoryCache
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
            .build()

        Coil.setImageLoader {
            ImageLoader.Builder(context)
                .components { add(imageInterceptor) }
                .memoryCache {
                    MemoryCache.Builder(context)
                        .maxSizePercent(0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .okHttpClient(coilOkHttpClient)
                .build()
        }
    }
}