package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceIdsSync

interface SourceIdsSyncDao : EntityDao<SourceIdsSync> {
    fun findRemoteId(
        sourceId: Long,
        localId: Long,
        type: SourceDataType
    ): Long?
}