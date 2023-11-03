package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.app.LastRequest
import com.gnoemes.shimori.data.app.Request

interface LastRequestDao : EntityDao<LastRequest> {
    suspend fun lastRequest(request: Request, entityId: Long): LastRequest?
    suspend fun requestCount(request: Request, entityId: Long): Int
}