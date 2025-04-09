package com.gnoemes.shimori.source.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.fragment.MangaShort
import com.gnoemes.shimori.source.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class MangaShortMapper(
    private val values: ShikimoriValues
) : Mapper<MangaShort, SManga?> {

    override fun map(from: MangaShort): SManga? {
        val type = from.kind?.toSourceType() ?: return null

        return SManga(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.english,
            image = from.poster?.posterShort?.toSourceType(),
            type = SourceDataType.Manga,
            mangaType = type,
            url = from.url.appendHostIfNeed(values),
            rating = from.score,
            status = from.status.toSourceType(),
            chapters = from.chapters,
            volumes = from.volumes,
            dateAired = from.airedOn?.date?.toLocalDate(),
            dateReleased = from.releasedOn?.date?.toLocalDate(),
            ageRating = null,
        )
    }
}