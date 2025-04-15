package com.gnoemes.shimori.source.shikimori.sources

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.source.catalogue.GenreDataSource
import com.gnoemes.shimori.source.model.SGenre
import com.gnoemes.shimori.source.shikimori.AnimeGenresQuery
import com.gnoemes.shimori.source.shikimori.MangaGenresQuery
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.GenreResponseToSGenreMapper
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriGenreDataSource(
    private val api: ShikimoriApi,
    private val genreMapper: GenreResponseToSGenreMapper
) : GenreDataSource {

    private suspend fun getAnimeGenres(): List<SGenre> {
        return api.apollo.query(AnimeGenresQuery()).dataAssertNoErrors
            .genres.map { it.genre }.let { genreMapper.forLists().invoke(it) }
    }

    private suspend fun getMangaGenres(): List<SGenre> {
        return api.apollo.query(MangaGenresQuery()).dataAssertNoErrors
            .genres.map { it.genre }.let { genreMapper.forLists().invoke(it) }
    }

    override suspend fun getAll(): List<SGenre> {
        return (getAnimeGenres().toSet() + getMangaGenres().toSet()).toList()
    }
}