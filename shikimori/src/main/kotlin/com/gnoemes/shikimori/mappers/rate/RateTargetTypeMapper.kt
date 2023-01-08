package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.common.ShikimoriContentType
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.core.mappers.TwoWayMapper

internal class RateTargetTypeMapper : TwoWayMapper<ShikimoriContentType?, TrackTargetType?> {

    override suspend fun map(from: ShikimoriContentType?): TrackTargetType? = when (from) {
        ShikimoriContentType.ANIME -> TrackTargetType.ANIME
        ShikimoriContentType.MANGA -> TrackTargetType.MANGA
        ShikimoriContentType.RANOBE -> TrackTargetType.RANOBE
        else -> null
    }

    override suspend fun mapInverse(from: TrackTargetType?): ShikimoriContentType? = when (from) {
        TrackTargetType.ANIME -> ShikimoriContentType.ANIME
        //shikimori doesn't support ranobe type
        TrackTargetType.MANGA, TrackTargetType.RANOBE -> ShikimoriContentType.MANGA
        else -> null
    }
}