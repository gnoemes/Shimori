package com.gnoemes.shikimori

import com.gnoemes.shimori.data.core.sources.*
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.TrackSource

class ShikimoriSource(
    animeSource: AnimeDataSource,
    mangaSource: MangaDataSource,
    ranobeSource: RanobeDataSource,
    characterSource: CharacterDataSource,
    userSource: UserDataSource,
    trackSource: TrackDataSource
) : CatalogueSource, TrackSource {
    override val id: Long = ID
    override val name: String = "Shikimori"
    override val animeDataSource: AnimeDataSource = animeSource
    override val mangaDataSource: MangaDataSource = mangaSource
    override val ranobeDataSource: RanobeDataSource = ranobeSource
    override val characterDataSource: CharacterDataSource = characterSource
    override val userDataSource: UserDataSource = userSource
    override val trackDataSource: TrackDataSource = trackSource

    companion object {
        internal const val ID = 1L
    }
}