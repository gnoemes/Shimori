package com.gnoemes.shimori.source

import com.gnoemes.shimori.source.data.AnimeDataSource
import com.gnoemes.shimori.source.data.CharacterDataSource
import com.gnoemes.shimori.source.data.MangaDataSource
import com.gnoemes.shimori.source.data.RanobeDataSource

/**
 * Database for titles, characters, etc...
 */
interface CatalogueSource : Source {
    val animeDataSource: AnimeDataSource
    val mangaDataSource: MangaDataSource
    val ranobeDataSource: RanobeDataSource
    val characterDataSource: CharacterDataSource
}