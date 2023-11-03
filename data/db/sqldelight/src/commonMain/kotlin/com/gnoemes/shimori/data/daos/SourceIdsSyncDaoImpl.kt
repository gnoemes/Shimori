package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceIdsSync
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.logging.api.Logger
import comgnoemesshimoridatadb.data.Source_ids_sync
import me.tatarka.inject.annotations.Inject

@Inject
class SourceIdsSyncDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : SourceIdsSyncDao, SqlDelightEntityDao<SourceIdsSync> {

    override fun insert(entity: SourceIdsSync): Long {
        entity.let {
            db.sourceIdsSyncQueries.insert(
                entity.sourceId,
                entity.localId,
                entity.remoteId,
                entity.type.type
            )
        }

        return db.sourceIdsSyncQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: SourceIdsSync) {
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

    override fun delete(entity: SourceIdsSync) {
        entity.let {
            db.sourceIdsSyncQueries.delete(entity.id)
        }
    }

    override fun findRemoteId(sourceId: Long, localId: Long, type: SourceDataType): Long? {
        return db.sourceIdsSyncQueries
            .findRemoteId(sourceId, localId, type.type)
            .executeAsOneOrNull()
    }
}