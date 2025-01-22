package com.gnoemes.shimori

import com.gnoemes.shimori.app.core.inject.SharedApplicationComponent
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.preferences.ShimoriPreferences
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class DesktopApplicationComponent : SharedApplicationComponent,
    DefaultApplicationComponent {
    abstract val initializers: Set<AppInitializer>
    abstract val windowComponentFactory: WindowComponent.Factory
    abstract val preferences: ShimoriPreferences

    companion object
}