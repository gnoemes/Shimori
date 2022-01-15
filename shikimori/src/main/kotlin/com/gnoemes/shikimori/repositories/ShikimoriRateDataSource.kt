package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.mappers.rate.RateMapper
import com.gnoemes.shikimori.services.RateService
import com.gnoemes.shimori.base.extensions.bodyOrThrow
import com.gnoemes.shimori.base.extensions.withRetry
import com.gnoemes.shimori.data_base.mappers.forLists
import com.gnoemes.shimori.data_base.sources.RateDataSource
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.user.UserShort
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriRateDataSource @Inject constructor(
    private val service: RateService,
    private val mapper: RateMapper
) : RateDataSource {

    override suspend fun getRates(user: UserShort): List<Rate> {
        return withRetry {
            service.getUserRates(userId = user.shikimoriId!!)
                .awaitResponse()
                .let { mapper.forLists().invoke(it.bodyOrThrow()) }
        }
    }

    override suspend fun createRate(rate: Rate): Rate {
        return withRetry {
            service.createRate(UserRateCreateOrUpdateRequest(mapper.mapInverse(rate)))
                .awaitResponse()
                .let { mapper.map(it.bodyOrThrow()) }
        }
    }

    override suspend fun updateRate(rate: Rate): Rate {
        return withRetry {
            service.updateRate(
                rate.shikimoriId!!,
                UserRateCreateOrUpdateRequest(mapper.mapInverse(rate))
            )
                .awaitResponse()
                .let { mapper.map(it.bodyOrThrow()) }
        }
    }

    override suspend fun deleteRate(id: Long) {
        withRetry {
            service.deleteRate(id)
                .awaitResponse()
                .bodyOrThrow()
        }
    }
}