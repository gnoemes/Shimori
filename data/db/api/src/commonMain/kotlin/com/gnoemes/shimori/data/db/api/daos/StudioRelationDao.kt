package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.common.StudioRelation

interface StudioRelationDao : EntityDao<StudioRelation> {
    fun queryByTitle(
        sourceId: Long,
        targetId: Long
    ): List<StudioRelation>
}