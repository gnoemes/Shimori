package com.gnoemes.shimori.main

import android.content.Context
import com.gnoemes.shimori.base.AppNavigator
import com.gnoemes.shimori.base.di.PerActivity
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal abstract class MainBuilder {

    internal abstract fun mainActivity(): MainActivity
}

@Module(includes = [MainModuleBinds::class])
class MainModule {

    @Provides
    fun provideAppNavigator(activity: MainActivity) : AppNavigator = MainAppNavigator(activity)

}

@Module
abstract class MainModuleBinds {

    @Binds
    @PerActivity
    abstract fun bindContext(activity: MainActivity): Context
}