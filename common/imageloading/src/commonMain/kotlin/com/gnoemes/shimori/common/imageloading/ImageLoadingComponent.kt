package com.gnoemes.shimori.common.imageloading

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.intercept.Interceptor
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

expect interface ImageLoadingPlatformComponent

interface ImageLoadingComponent : ImageLoadingPlatformComponent {


    @Provides
    fun provideImageLoader(
        context: PlatformContext,
        interceptors: Set<Interceptor>,
        applicationInfo: ApplicationInfo,
        logger: Logger
    ): ImageLoader = shimoriLoader(
        context = context,
        interceptors = interceptors,
        applicationInfo = applicationInfo,
        logger = logger
    )

    @Provides
    @IntoSet
    fun bindImageLoaderCleanupInitializer(initializer: ImageLoaderCleanupInitializer): AppInitializer =
        initializer

    @Provides
    @IntoSet
    fun providerShimoriImageInterceptor(initializer: ShimoriImageInterceptor): Interceptor =
        initializer


}