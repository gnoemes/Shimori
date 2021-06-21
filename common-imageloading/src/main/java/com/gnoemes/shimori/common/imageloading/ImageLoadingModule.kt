package com.gnoemes.shimori.common.imageloading

import com.gnoemes.shimori.base.appinitializers.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
abstract class ImageLoadingModule {
    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(coil : CoilAppInitializer) : AppInitializer
}