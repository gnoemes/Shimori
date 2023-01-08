package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToAnimeWithRateMapper constructor(
    private val rateResponseToRateMapper: RateResponseToRateMapper,
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<RateResponse, AnimeWithTrack> {

    override suspend fun map(from: RateResponse): AnimeWithTrack {
        return AnimeWithTrack(
            entity = animeResponseMapper.map(from.anime!!),
            track = rateResponseToRateMapper.map(from to null),
            pinned = false
        )
    }
}