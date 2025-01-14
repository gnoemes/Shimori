package com.gnoemes.shimori.common.imageloading

import coil3.PlatformContext
import me.tatarka.inject.annotations.Provides
import okio.FileSystem

actual interface ImageLoadingPlatformComponent {
    @Provides
    fun providePlatformContext(): PlatformContext = PlatformContext.INSTANCE

    @Provides
    fun provideFileSystem(): FileSystem = FileSystem.SYSTEM
}