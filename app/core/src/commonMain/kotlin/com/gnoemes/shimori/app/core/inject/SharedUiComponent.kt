package com.gnoemes.shimori.app.core.inject

import com.gnoemes.shimori.base.inject.ActivityScope
import com.gnoemes.shimori.home.RootUiComponent
import com.slack.circuit.foundation.Circuit
import me.tatarka.inject.annotations.Provides

interface SharedUiComponent : RootUiComponent {

    @Provides
    @ActivityScope
    fun provideCircuit(
//        uiFactories : Set<Ui.Factory>,
//        presenterFactories : Set<Presenter.Factory>,
    ): Circuit = Circuit.Builder()
//        .addUiFactories(uiFactories)
//        .addPresenterFactories(presenterFactories)
        .addUiFactories(setOf())
        .addPresenterFactories(setOf())
        .build()
}