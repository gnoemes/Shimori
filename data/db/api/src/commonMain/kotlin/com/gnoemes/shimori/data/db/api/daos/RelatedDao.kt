package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.common.Related
import com.gnoemes.shimori.data.common.RelatedRelation
import com.gnoemes.shimori.data.track.TrackTargetType

interface RelatedDao : EntityDao<RelatedRelation> {
    fun queryByTitle(
        targetId: Long,
        targetType: TrackTargetType
    ): List<RelatedRelation>

    fun paging(
        targetId: Long,
        targetType: TrackTargetType,
    ): PagingSource<Int, Related>

}