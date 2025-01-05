package com.gnoemes.shimori

import android.app.Activity
import com.gnoemes.shimori.app.core.inject.SharedActivityComponent
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.home.ShimoriContent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesSubcomponent(UiScope::class)
@SingleIn(UiScope::class)
interface AndroidActivityComponent : SharedActivityComponent {
    override val activity: Activity
    abstract val shimoriContent: ShimoriContent

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun createUiComponent(activity: Activity): AndroidActivityComponent
    }

    companion object
}