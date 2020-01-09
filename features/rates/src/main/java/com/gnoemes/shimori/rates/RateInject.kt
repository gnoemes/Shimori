package com.gnoemes.shimori.rates

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RateBuilder {
    @ContributesAndroidInjector(modules = [
        RateAssistedModule::class
    ])
    internal abstract fun fragment(): RateFragment

}

@Module(includes = [AssistedInject_RateAssistedModule::class])
@AssistedModule
interface RateAssistedModule