package com.gnoemes.shimori.sources.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.sources.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.gnoemes.shimori.sources.shikimori.fragment.AnimeShort
import com.gnoemes.shimori.sources.shikimori.mappers.toInstant
import com.gnoemes.shimori.sources.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriImage
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeShortMapper(
    private val values: ShikimoriValues
) : Mapper<AnimeShort, Anime> {

    override fun map(from: AnimeShort): Anime {
        return Anime(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.english,
            image = from.poster?.posterShort?.toShimoriImage(),
            type = TrackTargetType.ANIME,
            animeType = from.kind?.toShimoriType(),
            url = from.url.appendHostIfNeed(values),
            rating = from.score,
            status = from.status.toShimoriType(),
            episodes = from.episodes,
            episodesAired = from.episodesAired,
            dateAired = from.airedOn?.date?.toLocalDate(),
            dateReleased = from.releasedOn?.date?.toLocalDate(),
            nextEpisodeDate = from.nextEpisodeAt?.toInstant(),
            ageRating = from.rating.toShimoriType(),
            duration = from.duration,
        )
    }
}