package com.gnoemes.shimori.data.repositories.lastrequests

import com.gnoemes.shimori.data.daos.LastRequestDao
import com.gnoemes.shimori.model.app.LastRequest
import com.gnoemes.shimori.model.app.Request
import org.joda.time.Instant


open class EntityLastRequestStore(
    private val request: Request,
    private val dao: LastRequestDao
) {
    suspend fun getRequestInstant(entityId: Long): Instant? {
        return dao.lastRequest(request, entityId)?.timestamp
    }
    suspend fun hasBeenRequested(entityId: Long): Boolean = dao.requestCount(request, entityId) > 0

    suspend fun isRequestBefore(entityId: Long, instant: Instant): Boolean {
        return getRequestInstant(entityId)?.isBefore(instant) ?: true
    }

    suspend fun updateLastRequest(entityId: Long, timestamp: Instant = Instant.now()) {
        dao.insert(LastRequest(request = request, entityId = entityId, timestamp = timestamp))
    }
}