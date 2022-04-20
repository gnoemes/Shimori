package com.gnoemes.shimori.data.base.database.daos

import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.entities.titles.ranobe.RanobeWithRate
import kotlinx.coroutines.flow.Flow

abstract class RanobeDao : EntityDao<Ranobe>() {
    abstract suspend fun queryById(id: Long): Ranobe?
    abstract suspend fun queryAll(): List<Ranobe>
    abstract suspend fun queryAllWithStatus(): List<RanobeWithRate>
    abstract suspend fun queryByStatus(status: RateStatus): List<RanobeWithRate>

    abstract fun observeById(id: Long): Flow<RanobeWithRate?>
    abstract fun observeByStatus(status: RateStatus): Flow<List<RanobeWithRate>>

    abstract fun paging(status: RateStatus, descending: Boolean, sortOption: RateSortOption)
}