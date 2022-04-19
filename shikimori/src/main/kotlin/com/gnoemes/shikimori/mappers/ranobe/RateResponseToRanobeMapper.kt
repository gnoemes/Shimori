package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.mappers.Mapper

internal class RateResponseToRanobeMapper constructor(
    private val ranobeResponseMapper: RanobeResponseMapper
) : Mapper<RateResponse, Ranobe> {

    override suspend fun map(from: RateResponse): Ranobe {
        return ranobeResponseMapper.map(from.manga!!)
    }
}