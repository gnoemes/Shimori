package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.shikimori.AnimeTracksQuery
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeTracksQueryToAnimeWithTrack(
    private val animeMapper: AnimeShortMapper,
) : Mapper<AnimeTracksQuery.UserRate, SAnime> {

    override fun map(from: AnimeTracksQuery.UserRate): SAnime {
        return SAnime(
            entity = from.animeUserRateWithModel.anime!!.animeShort.let { animeMapper.map(it) },
            track = from.animeUserRateWithModel.toSourceType(),
        )
    }
}
