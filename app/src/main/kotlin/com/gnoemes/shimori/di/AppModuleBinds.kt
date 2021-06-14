package com.gnoemes.shimori.di

import com.gnoemes.shimori.appinitializers.PreferencesInitializer
import com.gnoemes.shimori.appinitializers.ThreeTenBpInitializer
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.settings.ShimoriPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindPreferences(prefs: ShimoriPreferencesImpl): ShimoriPreferences

    @Binds
    @IntoSet
    abstract fun bindThreeTenBpInitializer(threeTenBp: ThreeTenBpInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindPreferencesInitializer(prefs: PreferencesInitializer): AppInitializer

}