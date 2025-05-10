package com.gnoemes.shimori.source.shikimori.mappers

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SRelated
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.fragment.Related
import com.gnoemes.shimori.source.shikimori.mappers.anime.AnimeShortMapper
import com.gnoemes.shimori.source.shikimori.mappers.manga.MangaShortMapper
import com.gnoemes.shimori.source.shikimori.mappers.ranobe.RanobeShortMapper
import me.tatarka.inject.annotations.Inject

@Inject
class RelatedToSRelatedMapper(
    private val animeMapper: AnimeShortMapper,
    private val mangaMapper: MangaShortMapper,
    private val ranobeMapper: RanobeShortMapper,
) : Mapper<Related, SRelated> {
    override fun map(from: Related): SRelated {

        val anime = if (from.anime != null) animeMapper.map(from.anime.animeShort) else null
        val manga = if (from.manga != null) mangaMapper.map(from.manga.mangaShort) else null
        val ranobe = if (from.manga != null) ranobeMapper.map(from.manga.mangaShort) else null

        val type = when {
            anime != null -> SourceDataType.Anime
            manga != null -> SourceDataType.Manga
            ranobe != null -> SourceDataType.Ranobe
            else -> throw IllegalArgumentException("Unknown type")
        }

        return SRelated(
            targetId = -1L,
            targetType = type,
            relationType = from.relationKind.rawValue,
            relationText = from.relationText,
            anime = anime,
            manga = manga ?: ranobe,
        )
    }
}