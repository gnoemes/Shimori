package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.app.LastRequest
import com.gnoemes.shimori.data.core.entities.app.Request

abstract class LastRequestDao : EntityDao<LastRequest>() {
    abstract suspend fun lastRequest(request: Request, entityId : Long) : LastRequest?
    abstract suspend fun requestCount(request: Request, entityId: Long) : Int
}