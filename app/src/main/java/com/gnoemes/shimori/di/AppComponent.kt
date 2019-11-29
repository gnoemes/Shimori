package com.gnoemes.shimori.di

import com.gnoemes.common.epoxy.EpoxyModule
import com.gnoemes.shikimori.ShikimoriModule
import com.gnoemes.shimori.ShimoriApplication
import com.gnoemes.shimori.main.MainBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            AppAssistedModule::class,
            EpoxyModule::class,
            NetworkModule::class,
            MainBuilder::class,
            ShikimoriModule::class
        ])
interface AppComponent : AndroidInjector<ShimoriApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: ShimoriApplication): AppComponent
    }
}