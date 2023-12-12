package com.gnoemes.shimori.data.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.shikimori.AnimeTracksQuery
import com.gnoemes.shimori.data.shikimori.mappers.toShimoriType
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeTracksQueryToAnimeWithTrack(
    private val animeMapper: AnimeShortMapper,
) : Mapper<AnimeTracksQuery.UserRate, AnimeWithTrack> {

    override fun map(from: AnimeTracksQuery.UserRate): AnimeWithTrack {
        return AnimeWithTrack(
            entity = from.animeUserRateWithModel.anime!!.animeShort.let { animeMapper.map(it) },
            track = from.animeUserRateWithModel.toShimoriType(),
            pinned = false
        )
    }
}
