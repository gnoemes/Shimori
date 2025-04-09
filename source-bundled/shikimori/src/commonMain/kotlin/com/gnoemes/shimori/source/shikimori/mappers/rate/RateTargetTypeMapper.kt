package com.gnoemes.shimori.source.shikimori.mappers.rate

import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.models.common.ShikimoriContentType
import me.tatarka.inject.annotations.Inject

@Inject
class RateTargetTypeMapper : TwoWayMapper<ShikimoriContentType?, SourceDataType?> {

    override fun map(from: ShikimoriContentType?): SourceDataType? = when (from) {
        ShikimoriContentType.ANIME -> SourceDataType.Anime
        ShikimoriContentType.MANGA -> SourceDataType.Manga
        ShikimoriContentType.RANOBE -> SourceDataType.Ranobe
        else -> null
    }

    override fun mapInverse(from: SourceDataType?): ShikimoriContentType? = when (from) {
        SourceDataType.Anime -> ShikimoriContentType.ANIME
        //shikimori doesn't support ranobe type
        SourceDataType.Manga, SourceDataType.Ranobe -> ShikimoriContentType.MANGA
        else -> null
    }
}