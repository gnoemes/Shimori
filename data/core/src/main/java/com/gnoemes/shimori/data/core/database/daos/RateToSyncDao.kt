package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.rate.RateToSync

abstract class RateToSyncDao : EntityDao<RateToSync>() {

    abstract suspend fun queryAll(): List<RateToSync>

    abstract suspend fun queryAllByTarget(target: SyncApi): List<RateToSync>

    abstract suspend fun queryByRateId(id : Long) : RateToSync?

}