package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.core.entities.app.SourceIdsSync
import com.gnoemes.shimori.data.db.ShimoriDB
import comgnoemesshimoridatadb.data.Source_ids_sync

internal class SourceIdsSyncDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : SourceIdsSyncDao() {

    override suspend fun insert(entity: SourceIdsSync) {
        entity.let {
            db.sourceIdsSyncQueries.insert(
                entity.sourceId,
                entity.localId,
                entity.remoteId,
                entity.type.type
            )
        }
    }

    override suspend fun update(entity: SourceIdsSync) {
        entity.let {
            db.sourceIdsSyncQueries.update(
                Source_ids_sync(
                    entity.id,
                    entity.sourceId,
                    entity.localId,
                    entity.remoteId,
                    entity.type.type
                )
            )
        }
    }

    override suspend fun delete(entity: SourceIdsSync) {
        entity.let {
            db.sourceIdsSyncQueries.delete(entity.id)
        }
    }

    override suspend fun findRemoteId(sourceId: Long, localId: Long, type: SourceDataType): Long? {
        return db.sourceIdsSyncQueries
            .findRemoteId(sourceId, localId, type.type)
            .executeAsOneOrNull()
    }
}