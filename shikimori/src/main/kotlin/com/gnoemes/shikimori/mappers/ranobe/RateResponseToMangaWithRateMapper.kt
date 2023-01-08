package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToRanobeWithRateMapper constructor(
    private val rateResponseToRateMapper: RateResponseToRateMapper,
    private val ranobeResponseMapper: RanobeResponseMapper
) : Mapper<RateResponse, RanobeWithTrack> {

    override suspend fun map(from: RateResponse): RanobeWithTrack {
        return RanobeWithTrack(
            entity = ranobeResponseMapper.map(from.manga!!),
            track = rateResponseToRateMapper.map(from to null),
            pinned = false
        )
    }
}