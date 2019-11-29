package com.gnoemes.shimori.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.gnoemes.common.di.ViewModelKey
import com.gnoemes.shimori.base.di.PerActivity
import com.gnoemes.shimori.di.ViewModelBuilder
import com.gnoemes.shimori.search.SearchBuilder
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainBuilder {
    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class,
        SearchBuilder::class
    ])
    abstract fun mainAcitivity(): MainActivity

    @Binds
    @PerActivity
    abstract fun bindContext(activity: MainActivity): Context

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}