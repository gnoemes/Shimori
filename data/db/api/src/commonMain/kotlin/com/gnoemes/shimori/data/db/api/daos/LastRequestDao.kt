package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.app.LastRequest
import com.gnoemes.shimori.data.app.Request

interface LastRequestDao : EntityDao<LastRequest> {
    fun lastRequest(request: Request, entityId: Long): LastRequest?
    fun requestCount(request: Request, entityId: Long): Int
}