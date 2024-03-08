package com.gnoemes.shimori.data.source

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.source.catalogue.SourceCatalogueComponent
import com.gnoemes.shimori.data.source.track.SourceTrackComponent
import com.gnoemes.shimori.sources.shikimori.Shikimori
import com.gnoemes.shimori.sources.shikimori.ShikimoriComponent
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriAnimeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriCharacterDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriMangaDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriRanobeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriTrackDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriUserDataSource
import me.tatarka.inject.annotations.Provides


interface SourcesComponent :
    ShikimoriComponent,
    SourceCatalogueComponent,
    SourceTrackComponent {

    @ApplicationScope
    @Provides
    fun provideShikimori(
        values: ShikimoriValues,
        anime: ShikimoriAnimeDataSource,
        manga: ShikimoriMangaDataSource,
        ranobe: ShikimoriRanobeDataSource,
        character: ShikimoriCharacterDataSource,
        user: ShikimoriUserDataSource,
        track: ShikimoriTrackDataSource,
    ): Shikimori =
        Shikimori(
            SourceIds.SHIKIMORI,
            values, anime, manga, ranobe,
            character, user, track
        )


}