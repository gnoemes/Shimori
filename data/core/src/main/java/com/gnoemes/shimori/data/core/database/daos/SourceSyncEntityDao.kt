package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.app.SourceDataType

abstract class SourceSyncEntityDao<in E : ShimoriEntity>(
    val syncDataType: SourceDataType
) {
    abstract suspend fun insert(sourceId: Long, remote: E)
    abstract suspend fun update(sourceId: Long, remote: E, local: E)
    abstract suspend fun delete(sourceId: Long, local: E)
}