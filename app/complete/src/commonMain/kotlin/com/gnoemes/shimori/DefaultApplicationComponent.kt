package com.gnoemes.shimori

import com.gnoemes.shimori.base.entities.Flavor
import me.tatarka.inject.annotations.Provides

interface DefaultApplicationComponent {
    @Provides
    fun flavor() = Flavor.Complete
}