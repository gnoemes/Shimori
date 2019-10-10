package com.gnoemes.shimori.di

import android.app.Application
import com.gnoemes.shimori.ShimoriApplication
import com.gnoemes.shimori.appinitializers.AppCompatInitializer
import com.gnoemes.shimori.appinitializers.ArchInitializer
import com.gnoemes.shimori.appinitializers.JodaTimeInitializer
import com.gnoemes.shimori.appinitializers.PreferencesInitializer
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.settings.ShimoriPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindApplication(app: ShimoriApplication): Application

    @Singleton
    @Binds
    abstract fun bindPreferences(prefs: ShimoriPreferencesImpl): ShimoriPreferences

    @Binds
    @IntoSet
    abstract fun bindArchInitializer(arch: ArchInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindJodaInitializer(joda: JodaTimeInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindPreferencesInitializer(prefs: PreferencesInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindAppCompatInitializer(appCompat: AppCompatInitializer): AppInitializer
}