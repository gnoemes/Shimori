package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import kotlinx.coroutines.flow.Flow

abstract class RateDao : EntityDao<Rate>() {
    abstract suspend fun syncAll(data: List<Rate>)

    abstract fun observeById(id: Long): Flow<Rate?>
    abstract fun observeByShikimoriId(id: Long): Flow<Rate?>
    abstract fun observeByTarget(targetId: Long, targetType: RateTargetType): Flow<Rate?>
    abstract fun observeHasRates(): Flow<Boolean>
    abstract fun observeExistedStatuses(type: RateTargetType): Flow<List<RateStatus>>

    abstract suspend fun queryById(id: Long): Rate?
}