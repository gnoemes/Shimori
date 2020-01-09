package com.gnoemes.shimori.main

import android.content.Context
import com.gnoemes.shimori.base.di.PerActivity
import com.gnoemes.shimori.calendar.CalendarBuilder
import com.gnoemes.shimori.rates.RateBuilder
import com.gnoemes.shimori.search.SearchBuilder
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class MainBuilder {
    @ContributesAndroidInjector(modules = [
        MainModule::class,
        SearchBuilder::class,
        CalendarBuilder::class,
        RateBuilder::class
    ])

    internal abstract fun mainActivity(): MainActivity
}

@Module(includes = [MainModuleBinds::class])
class MainModule {

}

@Module
abstract class MainModuleBinds {

    @Binds
    @PerActivity
    abstract fun bindContext(activity: MainActivity): Context
}