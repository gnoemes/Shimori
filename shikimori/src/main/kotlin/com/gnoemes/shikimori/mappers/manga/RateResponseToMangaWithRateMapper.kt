package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToMangaWithRateMapper constructor(
    private val rateResponseToRateMapper: RateResponseToRateMapper,
    private val mangaResponseMapper: MangaResponseMapper
) : Mapper<RateResponse, MangaWithRate> {

    override suspend fun map(from: RateResponse): MangaWithRate {
        return MangaWithRate(
            entity = mangaResponseMapper.map(from.manga!!),
            rate = rateResponseToRateMapper.map(from to null),
            pinned = false
        )
    }
}