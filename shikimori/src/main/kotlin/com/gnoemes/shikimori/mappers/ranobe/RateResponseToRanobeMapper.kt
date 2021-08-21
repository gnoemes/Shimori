package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.ranobe.Ranobe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RateResponseToRanobeMapper @Inject constructor(
    private val ranobeResponseMapper: RanobeResponseMapper
) : Mapper<RateResponse, Ranobe> {

    override suspend fun map(from: RateResponse): Ranobe {
        return ranobeResponseMapper.map(from.manga!!)
    }
}