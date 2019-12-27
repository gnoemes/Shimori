package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.rate.RateMapper
import com.gnoemes.shikimori.services.RateService
import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.base.extensions.toResult
import com.gnoemes.shimori.data_base.mappers.toListMapper
import com.gnoemes.shimori.data_base.sources.RateDataSource
import com.gnoemes.shimori.model.rate.Rate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriRateDataSource @Inject constructor(
    private val service: RateService,
    private val mapper: RateMapper
) : RateDataSource {

    override suspend fun getRates(userId: Long): Result<List<Rate>> {
        return service.getUserRates(userId = userId)
            .toResult(mapper.toListMapper())
    }

    override suspend fun createRate(rate: Rate) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateRate(rate: Rate) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteRate(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}