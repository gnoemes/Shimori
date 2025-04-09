package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.app.SourceIdsSync
import com.gnoemes.shimori.source.model.SourceDataType

interface SourceIdsSyncDao : EntityDao<SourceIdsSync> {
    fun findRemoteId(
        sourceId: Long,
        localId: Long,
        type: SourceDataType
    ): Long?

    fun findLocalId(
        sourceId: Long,
        remoteId: Long,
        type: SourceDataType
    ): Long?

    fun syncRemoteIds(
        sourceId: Long,
        localId: Long,
        remoteId: Long,
        sourceDataType: SourceDataType
    )

    fun deleteByLocalId(
        sourceId: Long,
        localId: Long,
        type: SourceDataType
    )
}