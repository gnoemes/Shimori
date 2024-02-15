package com.gnoemes.shimori.sources.shikimori.mappers.ranobe

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.sources.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.gnoemes.shimori.sources.shikimori.fragment.MangaShort
import com.gnoemes.shimori.sources.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriImage
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriRanobeType
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriType
import me.tatarka.inject.annotations.Inject

@Inject
class RanobeShortMapper(
    private val values: ShikimoriValues
) : Mapper<MangaShort, Ranobe?> {
    override fun map(from: MangaShort): Ranobe? {
        val type = from.kind?.toShimoriRanobeType() ?: return null

        return Ranobe(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.japanese,
            image = from.poster?.posterShort?.toShimoriImage(),
            type = TrackTargetType.ANIME,
            ranobeType = type,
            url = from.url.appendHostIfNeed(values),
            rating = from.score,
            status = from.status.toShimoriType(),
            chapters = from.chapters,
            volumes = from.volumes,
            dateAired = from.airedOn?.date?.toLocalDate(),
            dateReleased = from.releasedOn?.date?.toLocalDate(),
            ageRating = AgeRating.NONE,
        )
    }
}