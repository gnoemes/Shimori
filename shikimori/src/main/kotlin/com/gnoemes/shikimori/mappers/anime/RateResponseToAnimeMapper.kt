package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.mappers.Mapper

internal class RateResponseToAnimeMapper constructor(
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<RateResponse, Anime> {

    override suspend fun map(from: RateResponse): Anime {
        return animeResponseMapper.map(from.anime!!)
    }
}