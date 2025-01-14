package com.gnoemes.shimori.common.imageloading

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.intercept.Interceptor
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

expect interface ImageLoadingPlatformComponent

@ContributesTo(AppScope::class)
interface ImageLoadingComponent : ImageLoadingPlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
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

}