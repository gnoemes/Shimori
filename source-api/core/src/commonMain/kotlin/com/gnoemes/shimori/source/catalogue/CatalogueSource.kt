package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.Source

/**
 * Database for titles, characters, etc...
 */
interface CatalogueSource : Source {
    val animeDataSource: AnimeDataSource
    val mangaDataSource: MangaDataSource
    val ranobeDataSource: RanobeDataSource
    val characterDataSource: CharacterDataSource
    val personDataSource: PersonDataSource
    val genreDateSource: GenreDataSource
    val studioDataSource: StudioDataSource
}