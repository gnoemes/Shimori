package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.LastRequestDao
import com.gnoemes.shimori.data.core.entities.app.LastRequest
import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.LastRequestDAO
import com.gnoemes.shimori.data.shared.LastRequestMapper

class LastRequestDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : LastRequestDao() {
    override suspend fun insert(entity: LastRequest) {
        entity.let {
            db.lastRequestQueries.insert(
                it.request,
                it.entityId,
                it.timeStamp
            )
        }
    }

    override suspend fun update(entity: LastRequest) {
        entity.let {
            db.lastRequestQueries.update(
                LastRequestDAO(
                    it.id,
                    it.request,
                    it.entityId,
                    it.timeStamp
                )
            )
        }
    }

    override suspend fun delete(entity: LastRequest) {
        db.lastRequestQueries.deleteById(entity.id)
    }

    override suspend fun lastRequest(request: Request, entityId: Long): LastRequest? {
        return db.lastRequestQueries.lastRequest(request, entityId)
            .executeAsOneOrNull()
            ?.let { LastRequestMapper.map(it) }
    }

    override suspend fun requestCount(request: Request, entityId: Long): Int {
        return db.lastRequestQueries.requestCount(request, entityId)
            .executeAsOneOrNull()
            ?.toInt() ?: 0
    }
}