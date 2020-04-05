package com.gnoemes.shimori.rates

import com.gnoemes.shimori.rates.edit.RateEditDialogFragment
import com.gnoemes.shimori.rates.sort.RateSortDialogFragment
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RateBuilder {
    @ContributesAndroidInjector(modules = [
        RateAssistedModule::class
    ])
    internal abstract fun fragment(): RateFragment

    @ContributesAndroidInjector(modules = [
        RateAssistedModule::class
    ])
    internal abstract fun sortDialog() : RateSortDialogFragment

    @ContributesAndroidInjector(modules = [
        RateAssistedModule::class
    ])
    internal abstract fun editDialog() : RateEditDialogFragment
}

@Module(includes = [AssistedInject_RateAssistedModule::class])
@AssistedModule
interface RateAssistedModule