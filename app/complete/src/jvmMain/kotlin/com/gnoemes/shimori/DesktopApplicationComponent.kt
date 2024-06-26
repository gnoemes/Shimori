package com.gnoemes.shimori

import com.gnoemes.shimori.app.core.appinitializers.AppInitializers
import com.gnoemes.shimori.app.core.inject.SharedApplicationComponent
import com.gnoemes.shimori.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Component

@Component
@ApplicationScope
abstract class DesktopApplicationComponent :
    SharedApplicationComponent,
    DefaultApplicationComponent {
    abstract val initializers: AppInitializers

    companion object
}