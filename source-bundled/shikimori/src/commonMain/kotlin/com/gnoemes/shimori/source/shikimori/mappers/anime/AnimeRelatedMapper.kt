package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.AnimeRelatedQuery
import com.gnoemes.shimori.source.shikimori.mappers.RelatedToSRelatedMapper
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeRelatedMapper(
    private val relatedMapper: RelatedToSRelatedMapper,
) : Mapper<AnimeRelatedQuery.Anime, SAnime> {

    override fun map(from: AnimeRelatedQuery.Anime): SAnime {
        val related = from.related
            ?.asSequence()
            ?.map { it.related }
            ?.map { relatedMapper.map(it) }
            ?.map { it.copy(targetId = from.id.toLong(), targetType = SourceDataType.Anime) }
            ?.toList()

        return SAnime(
            id = from.id.toLong(),
            related = related
        )
    }
}