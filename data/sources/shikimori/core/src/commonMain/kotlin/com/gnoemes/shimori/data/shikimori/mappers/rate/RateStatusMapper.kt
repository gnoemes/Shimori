package com.gnoemes.shimori.data.shikimori.mappers.rate

import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.data.shikimori.models.rates.ShikimoriRateStatus
import com.gnoemes.shimori.data.track.TrackStatus
import me.tatarka.inject.annotations.Inject

@Inject
class RateStatusMapper : TwoWayMapper<ShikimoriRateStatus?, TrackStatus?> {

    override fun map(from: ShikimoriRateStatus?): TrackStatus? = when (from) {
        ShikimoriRateStatus.PLANNED -> TrackStatus.PLANNED
        ShikimoriRateStatus.WATCHING -> TrackStatus.WATCHING
        ShikimoriRateStatus.REWATCHING -> TrackStatus.REWATCHING
        ShikimoriRateStatus.COMPLETED -> TrackStatus.COMPLETED
        ShikimoriRateStatus.ON_HOLD -> TrackStatus.ON_HOLD
        ShikimoriRateStatus.DROPPED -> TrackStatus.DROPPED
        else -> null
    }

    override fun mapInverse(from: TrackStatus?): ShikimoriRateStatus? = when (from) {
        TrackStatus.PLANNED -> ShikimoriRateStatus.PLANNED
        TrackStatus.WATCHING -> ShikimoriRateStatus.WATCHING
        TrackStatus.REWATCHING -> ShikimoriRateStatus.REWATCHING
        TrackStatus.COMPLETED -> ShikimoriRateStatus.COMPLETED
        TrackStatus.ON_HOLD -> ShikimoriRateStatus.ON_HOLD
        TrackStatus.DROPPED -> ShikimoriRateStatus.DROPPED
        else -> null
    }
}