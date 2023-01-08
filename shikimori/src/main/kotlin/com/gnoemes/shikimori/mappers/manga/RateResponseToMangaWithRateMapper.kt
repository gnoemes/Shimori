package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToMangaWithRateMapper constructor(
    private val rateResponseToRateMapper: RateResponseToRateMapper,
    private val mangaResponseMapper: MangaResponseMapper
) : Mapper<RateResponse, MangaWithTrack> {

    override suspend fun map(from: RateResponse): MangaWithTrack {
        return MangaWithTrack(
            entity = mangaResponseMapper.map(from.manga!!),
            track = rateResponseToRateMapper.map(from to null),
            pinned = false
        )
    }
}