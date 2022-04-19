package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus
import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType

internal interface RateService {

    suspend fun userRates(
        userId: Long,
        targetId: Long? = null,
        targetType: RateTargetType? = null,
        status: ShikimoriRateStatus? = null,
        page: Int? = null,
        limit: Int? = null
    ): List<UserRateResponse>

    suspend fun get(
        id: Long
    ): UserRateResponse

    suspend fun delete(
        id: Long
    )

    suspend fun create(
        request: UserRateCreateOrUpdateRequest
    ): UserRateResponse

    suspend fun update(
        id: Long,
        request: UserRateCreateOrUpdateRequest,
    ): UserRateResponse

    suspend fun increment(
        id: Long
    )
}