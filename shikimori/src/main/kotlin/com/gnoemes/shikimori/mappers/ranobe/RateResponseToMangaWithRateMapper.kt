package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToRanobeWithRateMapper constructor(
    private val rateResponseToRateMapper: RateResponseToRateMapper,
    private val ranobeResponseMapper: RanobeResponseMapper
) : Mapper<RateResponse, RanobeWithRate> {

    override suspend fun map(from: RateResponse): RanobeWithRate {
        return RanobeWithRate(
            entity = ranobeResponseMapper.map(from.manga!!),
            rate = rateResponseToRateMapper.map(from to null),
            pinned = false
        )
    }
}