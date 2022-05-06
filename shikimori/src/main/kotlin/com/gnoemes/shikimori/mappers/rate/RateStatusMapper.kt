package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.mappers.TwoWayMapper

internal class RateStatusMapper : TwoWayMapper<ShikimoriRateStatus?, RateStatus?> {

    override suspend fun map(from: ShikimoriRateStatus?): RateStatus? = when (from) {
        ShikimoriRateStatus.PLANNED -> RateStatus.PLANNED
        ShikimoriRateStatus.WATCHING -> RateStatus.WATCHING
        ShikimoriRateStatus.REWATCHING -> RateStatus.REWATCHING
        ShikimoriRateStatus.COMPLETED -> RateStatus.COMPLETED
        ShikimoriRateStatus.ON_HOLD -> RateStatus.ON_HOLD
        ShikimoriRateStatus.DROPPED -> RateStatus.DROPPED
        else -> null
    }

    override suspend fun mapInverse(from: RateStatus?): ShikimoriRateStatus? = when (from) {
        RateStatus.PLANNED -> ShikimoriRateStatus.PLANNED
        RateStatus.WATCHING -> ShikimoriRateStatus.WATCHING
        RateStatus.REWATCHING -> ShikimoriRateStatus.REWATCHING
        RateStatus.COMPLETED -> ShikimoriRateStatus.COMPLETED
        RateStatus.ON_HOLD -> ShikimoriRateStatus.ON_HOLD
        RateStatus.DROPPED -> ShikimoriRateStatus.DROPPED
        else -> null
    }
}