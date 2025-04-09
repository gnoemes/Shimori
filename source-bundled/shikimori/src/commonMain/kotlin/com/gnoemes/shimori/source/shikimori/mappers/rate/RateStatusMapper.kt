package com.gnoemes.shimori.source.shikimori.mappers.rate

import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.shikimori.models.rates.ShikimoriRateStatus
import me.tatarka.inject.annotations.Inject

@Inject
class RateStatusMapper : TwoWayMapper<ShikimoriRateStatus?, STrackStatus?> {

    override fun map(from: ShikimoriRateStatus?): STrackStatus? = when (from) {
        ShikimoriRateStatus.PLANNED -> STrackStatus.Planned
        ShikimoriRateStatus.WATCHING -> STrackStatus.Watching
        ShikimoriRateStatus.REWATCHING -> STrackStatus.ReWatching
        ShikimoriRateStatus.COMPLETED -> STrackStatus.Completed
        ShikimoriRateStatus.ON_HOLD -> STrackStatus.OnHold
        ShikimoriRateStatus.DROPPED -> STrackStatus.Dropped
        else -> null
    }

    override fun mapInverse(from: STrackStatus?): ShikimoriRateStatus? = when (from) {
        STrackStatus.Planned -> ShikimoriRateStatus.PLANNED
        STrackStatus.Watching -> ShikimoriRateStatus.WATCHING
        STrackStatus.ReWatching -> ShikimoriRateStatus.REWATCHING
        STrackStatus.Completed -> ShikimoriRateStatus.COMPLETED
        STrackStatus.OnHold -> ShikimoriRateStatus.ON_HOLD
        STrackStatus.Dropped -> ShikimoriRateStatus.DROPPED
        else -> null
    }
}