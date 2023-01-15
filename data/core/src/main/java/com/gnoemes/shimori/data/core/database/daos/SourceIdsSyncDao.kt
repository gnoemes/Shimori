package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.core.entities.app.SourceIdsSync

abstract class SourceIdsSyncDao : EntityDao<SourceIdsSync>() {
    abstract suspend fun findRemoteId(
        sourceId: Long,
        localId: Long,
        type: SourceDataType
    ): Long?
}