package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.RelatedRelation
import com.gnoemes.shimori.data.common.RelationType
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.source.model.SRelated
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject

@Inject
class SourceRelatedMapper : Mapper<SRelated, RelatedRelation> {
    override fun map(from: SRelated): RelatedRelation {

        val relatedType =
            if (from.anime != null) SourceDataType.Anime
            else if (from.manga != null) from.manga!!.type
            else throw IllegalArgumentException("Unknown related type")

        val relatedId = when (relatedType) {
            SourceDataType.Anime -> from.anime!!.id
            else -> from.manga!!.id
        }

        return RelatedRelation(
            id = 0L,
            type = RelationType.find(from.relationType),
            relation = from.relationText,
            targetId = from.targetId,
            targetType = TrackTargetType.find(from.targetType)!!,
            relatedId = relatedId,
            relatedType = TrackTargetType.find(relatedType)!!,
        )
    }
}