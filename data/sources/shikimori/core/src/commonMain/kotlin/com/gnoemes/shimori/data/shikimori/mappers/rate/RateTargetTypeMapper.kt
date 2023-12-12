package com.gnoemes.shimori.data.shikimori.mappers.rate

import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.data.shikimori.models.common.ShikimoriContentType
import com.gnoemes.shimori.data.track.TrackTargetType
import me.tatarka.inject.annotations.Inject

@Inject
class RateTargetTypeMapper : TwoWayMapper<ShikimoriContentType?, TrackTargetType?> {

    override fun map(from: ShikimoriContentType?): TrackTargetType? = when (from) {
        ShikimoriContentType.ANIME -> TrackTargetType.ANIME
        ShikimoriContentType.MANGA -> TrackTargetType.MANGA
        ShikimoriContentType.RANOBE -> TrackTargetType.RANOBE
        else -> null
    }

    override fun mapInverse(from: TrackTargetType?): ShikimoriContentType? = when (from) {
        TrackTargetType.ANIME -> ShikimoriContentType.ANIME
        //shikimori doesn't support ranobe type
        TrackTargetType.MANGA, TrackTargetType.RANOBE -> ShikimoriContentType.MANGA
        else -> null
    }
}