package com.gnoemes.shimori.data.repositories.lastrequest

import com.gnoemes.shimori.data.core.database.daos.LastRequestDao
import com.gnoemes.shimori.data.core.entities.app.LastRequest
import com.gnoemes.shimori.data.core.entities.app.Request
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

open class GroupLastRequestStore(
    private val request: Request,
    private val dao: LastRequestDao
) {

    suspend fun getRequestInstant(id: Long): Instant? {
        return dao.lastRequest(request, id)?.timeStamp
    }

    suspend fun isRequestBefore(instant: Instant): Boolean {
        return getRequestInstant(DEFAULT_ID)?.compareTo(instant)?.let { it < 0 } ?: true
    }

    suspend fun isRequestBefore(instant: Instant, id: Long): Boolean {
        return getRequestInstant(id)?.compareTo(instant)?.let { it < 0 } ?: true
    }

    suspend fun updateLastRequest(
        timestamp: Instant = Clock.System.now(),
        id: Long = DEFAULT_ID
    ) {
        dao.insert(LastRequest(request = request, entityId = id, timeStamp = timestamp, id = 0))
    }

    companion object {
        private const val DEFAULT_ID = 0L
        const val ALL_RATE_STATUSES_ID = 6L
    }

}