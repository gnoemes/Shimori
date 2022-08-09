package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

abstract class RanobeDao : EntityDao<Ranobe>() {
    abstract suspend fun queryById(id: Long): Ranobe?
    abstract suspend fun queryAll(): List<Ranobe>
    abstract suspend fun queryByStatus(status: RateStatus): List<RanobeWithRate>

    abstract fun observeById(id: Long): Flow<RanobeWithRate?>

    abstract fun paging(
        status: RateStatus,
        sort: RateSort,
    ): PagingSource<Long, PaginatedEntity>
}