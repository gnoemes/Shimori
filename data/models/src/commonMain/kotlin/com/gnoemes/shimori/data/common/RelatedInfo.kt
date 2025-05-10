package com.gnoemes.shimori.data.common

import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.track.TrackTargetType

data class RelatedInfo(
    val titleId: Long,
    val titleType: TrackTargetType,
    val relation: RelatedRelation,
    val title: ShimoriTitleEntity,
)