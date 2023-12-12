package com.gnoemes.shimori.data

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.shikimori.Shikimori
import com.gnoemes.shimori.source.TrackSource
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SourceTrackComponent {

    @IntoSet
    @ApplicationScope
    @Provides
    fun provideShikimoriTrackSource(source: Shikimori): TrackSource = source
}