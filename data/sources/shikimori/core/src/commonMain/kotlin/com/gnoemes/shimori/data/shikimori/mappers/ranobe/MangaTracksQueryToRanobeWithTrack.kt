package com.gnoemes.shimori.data.shikimori.mappers.ranobe

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.shikimori.MangaTracksQuery
import com.gnoemes.shimori.data.shikimori.mappers.toShimoriType
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import me.tatarka.inject.annotations.Inject

@Inject
class MangaTracksQueryToRanobeWithTrack(
    private val ranobeMapper: RanobeShortMapper,
) : Mapper<MangaTracksQuery.UserRate, RanobeWithTrack?> {

    override fun map(from: MangaTracksQuery.UserRate): RanobeWithTrack? {
        val entity = from.mangaUserRateWithModel.manga?.mangaShort?.let { ranobeMapper.map(it) } ?: return null

        return RanobeWithTrack(
            entity = entity,
            track = from.mangaUserRateWithModel.toShimoriType(),
            pinned = false
        )
    }
}
