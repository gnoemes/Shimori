package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.common.ShikimoriContentType
import com.gnoemes.shimori.data_base.mappers.TwoWayMapper
import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RateTargetTypeMapper @Inject constructor() : TwoWayMapper<ShikimoriContentType?, RateTargetType?> {

    override suspend fun map(from: ShikimoriContentType?): RateTargetType? = when(from) {
        ShikimoriContentType.ANIME -> RateTargetType.ANIME
        ShikimoriContentType.MANGA -> RateTargetType.MANGA
        ShikimoriContentType.RANOBE -> RateTargetType.RANOBE
        else -> null
    }

    override suspend fun mapInverse(from: RateTargetType?): ShikimoriContentType? = when(from) {
        RateTargetType.ANIME -> ShikimoriContentType.ANIME
        RateTargetType.MANGA -> ShikimoriContentType.MANGA
        RateTargetType.RANOBE -> ShikimoriContentType.RANOBE
        else -> null
    }
}