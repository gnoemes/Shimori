package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.common.GenreRelation
import com.gnoemes.shimori.data.track.TrackTargetType

abstract class GenreRelationDao : EntityDao<GenreRelation> {
    abstract fun queryRelationsByTitle(
        id: Long,
        type: TrackTargetType,
        sourceId: Long
    ): List<GenreRelation>
}