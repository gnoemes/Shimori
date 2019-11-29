package com.gnoemes.shimori.data.repositories.search

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import com.gnoemes.shimori.model.anime.Anime
import javax.inject.Inject

class SearchRepository @Inject constructor(
    @Shikimori private val source: AnimeDataSource
) {

    //TODO error processing or Result
    suspend fun search(): List<Anime> {
        return source.search()
    }

}