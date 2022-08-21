package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.mappers.rate.RateMapper
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.forLists
import com.gnoemes.shimori.data.core.sources.RateDataSource

internal class ShikimoriRateDataSource(
    private val shikimori: Shikimori,
    private val rateMapper: RateMapper,
) : RateDataSource {
    override suspend fun getRates(user: UserShort): List<Rate> {
        return shikimori.rate.userRates(user.shikimoriId)
            .let { rateMapper.forLists().invoke(it) }
    }

    override suspend fun createRate(rate: Rate): Rate {
        return shikimori.rate.create(
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(rate))
        ).let { rateMapper.map(it) }
    }

    override suspend fun updateRate(rate: Rate): Rate {
        return shikimori.rate.update(
            rate.shikimoriId,
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(rate))
        ).let { rateMapper.map(it) }
    }

    override suspend fun deleteRate(id: Long) {
        return shikimori.rate.delete(id)
    }
}