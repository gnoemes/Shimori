package com.gnoemes.shimori.sources.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.titles.MangaOrRanobeWithTrack
import com.gnoemes.shimori.sources.shikimori.MangaTracksQuery
import com.gnoemes.shimori.sources.shikimori.mappers.ranobe.RanobeShortMapper
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriType
import me.tatarka.inject.annotations.Inject

@Inject
class MangaOrRanobeTracksQueryToMangaWithTrack(
    private val mangaMapper: MangaShortMapper,
    private val ranobeMapper: RanobeShortMapper,
) : Mapper<MangaTracksQuery.UserRate, MangaOrRanobeWithTrack?> {

    override fun map(from: MangaTracksQuery.UserRate): MangaOrRanobeWithTrack? {
        val manga = from.mangaUserRateWithModel.manga?.mangaShort?.let { mangaMapper.map(it) }
        val ranobe = from.mangaUserRateWithModel.manga?.mangaShort?.let { ranobeMapper.map(it) }
        val entity = manga ?: ranobe

        if (entity == null) return null

        return MangaOrRanobeWithTrack(
            entity = entity,
            track = from.mangaUserRateWithModel.toShimoriType(
                isRanobe = entity.type.ranobe
            ),
        )
    }
}
