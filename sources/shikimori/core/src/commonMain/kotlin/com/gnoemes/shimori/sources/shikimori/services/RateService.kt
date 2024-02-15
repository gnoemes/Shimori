package com.gnoemes.shimori.sources.shikimori.services

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Query
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.sources.shikimori.models.rates.ShikimoriRateStatus
import com.gnoemes.shimori.sources.shikimori.models.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shimori.sources.shikimori.models.rates.UserRateResponse

internal interface RateService {

    suspend fun <D : Query.Data> graphql(query: Query<D>): ApolloResponse<D>
    suspend fun userRates(
        userId: Long,
        targetId: Long? = null,
        targetType: TrackTargetType? = null,
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