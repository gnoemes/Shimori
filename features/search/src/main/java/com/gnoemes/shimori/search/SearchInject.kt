package com.gnoemes.shimori.search

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchBuilder {
    @ContributesAndroidInjector(modules = [
        SearchAssistedModule::class
    ])
    internal abstract fun searchFragment(): SearchFragment
}

@Module(includes = [AssistedInject_SearchAssistedModule::class])
@AssistedModule
interface SearchAssistedModule