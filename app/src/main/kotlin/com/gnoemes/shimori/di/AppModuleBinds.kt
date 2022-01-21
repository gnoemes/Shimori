package com.gnoemes.shimori.di

import com.gnoemes.shimori.appinitializers.ThreeTenBpInitializer
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.settings.ShimoriSettings
import com.gnoemes.shimori.settings.ShimoriSettingsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModuleBinds {

    @Binds
    @Singleton
    abstract fun bindSettings(settings: ShimoriSettingsImpl): ShimoriSettings

    @Binds
    @IntoSet
    abstract fun bindThreeTenBpInitializer(threeTenBp: ThreeTenBpInitializer): AppInitializer

}