package com.gnoemes.shimori.data.syncer

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType

abstract class SyncedSourceStore(
    protected val syncDao: SourceIdsSyncDao,
    protected val logger: Logger,
    protected val type: SourceDataType
) {
    protected val tag = "SyncedSourceStore: $type"

    abstract fun <T> trySync(response: SourceResponse<T>)
    abstract fun <E> trySync(params: SourceParams, data: List<E>)
}