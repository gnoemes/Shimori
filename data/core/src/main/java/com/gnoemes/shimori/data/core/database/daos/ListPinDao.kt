package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.ListPin
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

abstract class ListPinDao : EntityDao<ListPin>() {
    abstract suspend fun pin(targetId: Long, targetType: RateTargetType, pin: Boolean): Boolean
    abstract suspend fun togglePin(targetId: Long, targetType: RateTargetType): Boolean

    abstract fun observePinExist(targetId: Long, targetType: RateTargetType): Flow<Boolean>
    abstract fun observePinsExist(): Flow<Boolean>

    abstract fun paging(
        sort: RateSort
    ) : PagingSource<Long, PaginatedEntity>
}