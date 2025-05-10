package com.gnoemes.shimori.source.shikimori.mappers.ranobe

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.MangaRelatedQuery
import com.gnoemes.shimori.source.shikimori.mappers.RelatedToSRelatedMapper
import me.tatarka.inject.annotations.Inject

@Inject
class RanobeRelatedMapper(
    private val relatedMapper: RelatedToSRelatedMapper,
) : Mapper<MangaRelatedQuery.Manga, SManga> {

    override fun map(from: MangaRelatedQuery.Manga): SManga {
        val related = from.related
            ?.asSequence()
            ?.map { it.related }
            ?.map { relatedMapper.map(it) }
            ?.map { it.copy(targetId = from.id.toLong(), targetType = SourceDataType.Ranobe) }
            ?.toList()

        return SManga(
            type = SourceDataType.Ranobe,
            id = from.id.toLong(),
            related = related
        )
    }
}