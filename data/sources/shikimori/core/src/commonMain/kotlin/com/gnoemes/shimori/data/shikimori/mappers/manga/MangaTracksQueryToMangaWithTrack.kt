package com.gnoemes.shimori.data.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.shikimori.MangaTracksQuery
import com.gnoemes.shimori.data.shikimori.mappers.toShimoriType
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import me.tatarka.inject.annotations.Inject

@Inject
class MangaTracksQueryToMangaWithTrack(
    private val mangaMapper: MangaShortMapper,
) : Mapper<MangaTracksQuery.UserRate, MangaWithTrack?> {

    override fun map(from: MangaTracksQuery.UserRate): MangaWithTrack? {
        val entity = from.mangaUserRateWithModel.manga?.mangaShort?.let { mangaMapper.map(it) } ?: return null

        return MangaWithTrack(
            entity = entity,
            track = from.mangaUserRateWithModel.toShimoriType(),
            pinned = false
        )
    }
}
