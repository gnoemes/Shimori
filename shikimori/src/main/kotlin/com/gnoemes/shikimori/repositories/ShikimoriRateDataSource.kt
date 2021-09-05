package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.mappers.rate.RateMapper
import com.gnoemes.shikimori.services.RateService
import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.base.extensions.toResult
import com.gnoemes.shimori.base.extensions.unitResult
import com.gnoemes.shimori.data_base.mappers.toLambda
import com.gnoemes.shimori.data_base.mappers.toListMapper
import com.gnoemes.shimori.data_base.sources.RateDataSource
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.user.UserShort
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriRateDataSource @Inject constructor(
    private val service: RateService,
    private val mapper: RateMapper
) : RateDataSource {

    override suspend fun getRates(user: UserShort): Result<List<Rate>> {
        return service.getUserRates(userId = user.shikimoriId!!)
            .toResult(mapper.toListMapper())
    }

    override suspend fun createRate(rate: Rate): Result<Rate> {
        return service.createRate(UserRateCreateOrUpdateRequest(mapper.mapInverse(rate)))
            .toResult(mapper.toLambda())
    }

    override suspend fun updateRate(rate: Rate): Result<Rate> {
        return service.updateRate(rate.shikimoriId!!, UserRateCreateOrUpdateRequest(mapper.mapInverse(rate)))
            .toResult(mapper.toLambda())
    }

    override suspend fun deleteRate(id: Long) : Result<Unit> {
        return service.deleteRate(id)
            .unitResult()
    }
}