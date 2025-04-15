package com.gnoemes.shimori.source.shikimori.services

import com.gnoemes.shimori.source.shikimori.models.common.ShikimoriTargetType
import com.gnoemes.shimori.source.shikimori.models.rates.ShikimoriRateStatus
import com.gnoemes.shimori.source.shikimori.models.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shimori.source.shikimori.models.rates.UserRateResponse

internal interface RateService {
    suspend fun userRates(
        userId: Long,
        targetId: Long? = null,
        targetType: ShikimoriTargetType? = null,
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