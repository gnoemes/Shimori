package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.sources.shikimori.Shikimori
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SourceCatalogueComponent {
    @IntoSet
    @ApplicationScope
    @Provides
    fun provideShikimoriCatalogueSource(source: Shikimori): CatalogueSource = source
}