package com.gnoemes.shimori

import android.app.Application
import com.gnoemes.shimori.app.core.appinitializers.AppInitializers
import com.gnoemes.shimori.app.core.inject.SharedApplicationComponent
import com.gnoemes.shimori.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(
    @get:Provides val application: Application,
) : SharedApplicationComponent,
    DefaultApplicationComponent {

    abstract val initializers: AppInitializers

    companion object
}