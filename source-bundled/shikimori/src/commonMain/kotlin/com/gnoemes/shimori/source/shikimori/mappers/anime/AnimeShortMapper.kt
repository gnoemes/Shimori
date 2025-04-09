package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.fragment.AnimeShort
import com.gnoemes.shimori.source.shikimori.mappers.toInstant
import com.gnoemes.shimori.source.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeShortMapper(
    private val values: ShikimoriValues
) : Mapper<AnimeShort, SAnime> {

    override fun map(from: AnimeShort): SAnime {
        return SAnime(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.english,
            image = from.poster?.posterShort?.toSourceType(),
            animeType = from.kind?.toSourceType(),
            url = from.url.appendHostIfNeed(values),
            rating = from.score,
            status = from.status.toSourceType(),
            episodes = from.episodes,
            episodesAired = from.episodesAired,
            dateAired = from.airedOn?.date?.toLocalDate(),
            dateReleased = from.releasedOn?.date?.toLocalDate(),
            nextEpisodeDate = from.nextEpisodeAt?.toInstant(),
            ageRating = from.rating.toSourceType(),
            duration = from.duration,
        )
    }
}