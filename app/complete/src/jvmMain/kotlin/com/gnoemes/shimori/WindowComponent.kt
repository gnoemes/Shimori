package com.gnoemes.shimori

import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.home.ShimoriContent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesSubcomponent(UiScope::class)
@SingleIn(UiScope::class)
interface WindowComponent : DefaultUiComponent {
    abstract val shimoriContent: ShimoriContent

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun createUiComponent(): WindowComponent
    }

    companion object
}