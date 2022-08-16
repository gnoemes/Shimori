package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToAnimeWithRateMapper constructor(
    private val rateResponseToRateMapper: RateResponseToRateMapper,
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<RateResponse, AnimeWithRate> {

    override suspend fun map(from: RateResponse): AnimeWithRate {
        return AnimeWithRate(
            entity = animeResponseMapper.map(from.anime!!),
            rate = rateResponseToRateMapper.map(from),
            pinned = false
        )
    }
}