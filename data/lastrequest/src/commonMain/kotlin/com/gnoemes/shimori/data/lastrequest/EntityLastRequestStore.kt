package com.gnoemes.shimori.data.lastrequest

import com.gnoemes.shimori.data.app.LastRequest
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.db.api.daos.LastRequestDao
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration

@Inject
class EntityLastRequestStore(
    private val dao: LastRequestDao
) {

    fun getRequestInstant(request: Request, entityId: Long): Instant? {
        return dao.lastRequest(request, entityId)?.timeStamp
    }

    fun isRequestExpired(request: Request, entityId: Long, threshold: Duration): Boolean {
        return isRequestBefore(request, entityId, Clock.System.now() - threshold)
    }

    fun isRequestValid(request: Request, entityId: Long, threshold: Duration): Boolean {
        return !isRequestExpired(request, entityId, threshold)
    }

    fun isRequestBefore(request: Request, entityId: Long, instant: Instant): Boolean {
        return getRequestInstant(request, entityId)?.let { it < instant } ?: true
    }

    fun updateLastRequest(
        request: Request,
        entityId: Long,
        timestamp: Instant = Clock.System.now()
    ) {
        dao.upsert(
            LastRequest(
                request = request,
                entityId = entityId,
                timeStamp = timestamp
            )
        )
    }
}