package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.anime.Anime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RateResponseToAnimeMapper @Inject constructor(
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<RateResponse, Anime> {

    override suspend fun map(from: RateResponse): Anime {
        return animeResponseMapper.map(from.anime!!)
    }
}