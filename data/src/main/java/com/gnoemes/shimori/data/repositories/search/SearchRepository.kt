package com.gnoemes.shimori.data.repositories.search

import com.gnoemes.shimori.base.data.entities.Success
import com.gnoemes.shimori.data.extensions.toListMapper
import com.gnoemes.shimori.data.extensions.toResult
import com.gnoemes.shimori.data.mappers.AnimeResponseMapper
import com.gnoemes.shimori.data.remote.AnimeApi
import com.gnoemes.shimori.model.anime.Anime
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: AnimeApi,
    private val animeMapper: AnimeResponseMapper
) {

    //TODO error processing or Result
    suspend fun search(): List<Anime> {
        val filter = mapOf(
                "page" to "1",
                "limit" to "50"
        )
        val result = api.search(filter)
            .toResult(animeMapper.toListMapper())

        if (result is Success) {
            return result.data
        }
        return emptyList()
    }

}