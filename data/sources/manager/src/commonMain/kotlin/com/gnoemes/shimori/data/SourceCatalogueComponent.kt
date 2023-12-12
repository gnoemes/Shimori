package com.gnoemes.shimori.data

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.shikimori.Shikimori
import com.gnoemes.shimori.source.CatalogueSource
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SourceCatalogueComponent {
    @IntoSet
    @ApplicationScope
    @Provides
    fun provideShikimoriCatalogueSource(source: Shikimori): CatalogueSource = source
}