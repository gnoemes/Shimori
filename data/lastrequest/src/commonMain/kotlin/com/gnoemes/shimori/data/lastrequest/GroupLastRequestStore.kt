package com.gnoemes.shimori.data.lastrequest

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.app.LastRequest
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.db.api.daos.LastRequestDao
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration

@Inject
@ApplicationScope
class GroupLastRequestStore(
    private val dao: LastRequestDao
) {

    fun getRequestInstant(request: Request): Instant? {
        return dao.lastRequest(request, DEFAULT_ID)?.timeStamp
    }

    fun isRequestExpired(request: Request, threshold: Duration): Boolean {
        return isRequestBefore(request, Clock.System.now() - threshold)
    }

    fun isRequestValid(request: Request, threshold: Duration): Boolean {
        return !isRequestExpired(request, threshold)
    }

    fun isRequestBefore(request: Request, instant: Instant): Boolean {
        return getRequestInstant(request)?.let { it < instant } ?: true
    }

    fun updateLastRequest(request: Request, timestamp: Instant = Clock.System.now()) {
        dao.upsert(
            LastRequest(
                request = request,
                entityId = DEFAULT_ID,
                timeStamp = timestamp
            )
        )
    }

    companion object {
        private const val DEFAULT_ID = 0L
    }

}