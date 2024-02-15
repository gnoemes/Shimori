package com.gnoemes.shimori.data.source.track

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.source.TrackSource
import com.gnoemes.shimori.sources.shikimori.Shikimori
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SourceTrackComponent {

    @IntoSet
    @ApplicationScope
    @Provides
    fun provideShikimoriTrackSource(source: Shikimori): TrackSource = source
}