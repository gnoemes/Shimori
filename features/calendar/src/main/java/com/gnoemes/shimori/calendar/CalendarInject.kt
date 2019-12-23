package com.gnoemes.shimori.calendar

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CalendarBuilder {
    @ContributesAndroidInjector(modules = [
        CalendarAssistedModule::class
    ])
    internal abstract fun fragment(): CalendarFragment
}

@Module(includes = [AssistedInject_CalendarAssistedModule::class])
@AssistedModule
interface CalendarAssistedModule