package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.mappers.TwoWayMapper

internal class RateStatusMapper : TwoWayMapper<ShikimoriRateStatus?, TrackStatus?> {

    override suspend fun map(from: ShikimoriRateStatus?): TrackStatus? = when (from) {
        ShikimoriRateStatus.PLANNED -> TrackStatus.PLANNED
        ShikimoriRateStatus.WATCHING -> TrackStatus.WATCHING
        ShikimoriRateStatus.REWATCHING -> TrackStatus.REWATCHING
        ShikimoriRateStatus.COMPLETED -> TrackStatus.COMPLETED
        ShikimoriRateStatus.ON_HOLD -> TrackStatus.ON_HOLD
        ShikimoriRateStatus.DROPPED -> TrackStatus.DROPPED
        else -> null
    }

    override suspend fun mapInverse(from: TrackStatus?): ShikimoriRateStatus? = when (from) {
        TrackStatus.PLANNED -> ShikimoriRateStatus.PLANNED
        TrackStatus.WATCHING -> ShikimoriRateStatus.WATCHING
        TrackStatus.REWATCHING -> ShikimoriRateStatus.REWATCHING
        TrackStatus.COMPLETED -> ShikimoriRateStatus.COMPLETED
        TrackStatus.ON_HOLD -> ShikimoriRateStatus.ON_HOLD
        TrackStatus.DROPPED -> ShikimoriRateStatus.DROPPED
        else -> null
    }
}