package com.gnoemes.shimori.source

import com.gnoemes.shimori.data.core.sources.AnimeDataSource
import com.gnoemes.shimori.data.core.sources.CharacterDataSource
import com.gnoemes.shimori.data.core.sources.MangaDataSource
import com.gnoemes.shimori.data.core.sources.RanobeDataSource

/**
 * Database for titles, characters, etc...
 */
interface CatalogueSource : Source {
    val animeDataSource: AnimeDataSource
    val mangaDataSource: MangaDataSource
    val ranobeDataSource: RanobeDataSource
    val characterDataSource: CharacterDataSource
}