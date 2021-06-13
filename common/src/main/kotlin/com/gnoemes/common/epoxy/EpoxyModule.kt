package com.gnoemes.common.epoxy

import com.gnoemes.shimori.base.appinitializers.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class EpoxyModule {

    @Binds
    @IntoSet
    abstract fun bindEpoxyInitializer(epoxy: EpoxyInitializer): AppInitializer
}