package com.gnoemes.shimori.source.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.MangaTracksQuery
import com.gnoemes.shimori.source.shikimori.mappers.ranobe.RanobeShortMapper
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class MangaOrRanobeTracksQueryToMangaWithTrack(
    private val mangaMapper: MangaShortMapper,
    private val ranobeMapper: RanobeShortMapper,
) : Mapper<MangaTracksQuery.UserRate, SManga?> {

    override fun map(from: MangaTracksQuery.UserRate): SManga? {
        val manga = from.mangaUserRateWithModel.manga?.mangaShort?.let { mangaMapper.map(it) }
        val ranobe = from.mangaUserRateWithModel.manga?.mangaShort?.let { ranobeMapper.map(it) }
        val entity = manga ?: ranobe

        if (entity == null) return null

        return SManga(
            entity = entity,
            track = from.mangaUserRateWithModel.toSourceType(
                isRanobe = entity.type == SourceDataType.Ranobe
            ),
        )
    }
}
