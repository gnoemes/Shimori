package com.gnoemes.shimori.data

import com.gnoemes.shimori.base.inject.ApplicationScope
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


interface SourceManagerComponent :
    SourceCatalogueComponent,
    SourceTrackComponent,
    ShikimoriComponent {

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