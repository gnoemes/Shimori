package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.data.app.LastRequest
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.db.api.daos.LastRequestDao
import com.gnoemes.shimori.data.util.LastRequestDAO
import com.gnoemes.shimori.data.util.LastRequestMapper
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = LastRequestDao::class)
class LastRequestDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
) : LastRequestDao, SqlDelightEntityDao<LastRequest> {
    override fun insert(entity: LastRequest): Long {
        entity.let {
            db.lastRequestQueries.insert(
                it.request,
                it.entityId,
                it.timeStamp
            )
        }
        return db.lastRequestQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: LastRequest) {
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

    override fun delete(entity: LastRequest) {
        db.lastRequestQueries.deleteById(entity.id)
    }

    override fun lastRequest(request: Request, entityId: Long): LastRequest? {
        return db.lastRequestQueries.lastRequest(request, entityId)
            .executeAsOneOrNull()
            ?.let { LastRequestMapper.map(it) }
    }

    override fun requestCount(request: Request, entityId: Long): Int {
        return db.lastRequestQueries.requestCount(request, entityId)
            .executeAsOneOrNull()
            ?.toInt() ?: 0
    }
}