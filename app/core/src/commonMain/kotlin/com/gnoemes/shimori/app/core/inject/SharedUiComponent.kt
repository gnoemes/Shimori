package com.gnoemes.shimori.app.core.inject

import com.gnoemes.shimori.base.inject.UiScope
import com.slack.circuit.foundation.Circuit
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(UiScope::class)
interface SharedUiComponent {

    @Provides
    fun provideCircuit(
//        uiFactories: Set<Ui.Factory>,
//        presenterFactories: Set<Presenter.Factory>,
    ): Circuit = Circuit.Builder()
//        .addUiFactories(uiFactories)
//        .addPresenterFactories(presenterFactories)
        .addUiFactories(setOf())
        .addPresenterFactories(setOf())
        .build()
}