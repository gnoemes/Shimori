package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.ListPin
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

abstract class ListPinDao : EntityDao<ListPin>() {
    abstract suspend fun pin(targetId: Long, targetType: TrackTargetType, pin: Boolean): Boolean
    abstract suspend fun togglePin(targetId: Long, targetType: TrackTargetType): Boolean

    abstract fun observePinExist(targetId: Long, targetType: TrackTargetType): Flow<Boolean>
    abstract fun observePinsExist(): Flow<Boolean>

    abstract fun paging(
        sort: ListSort
    ) : PagingSource<Long, PaginatedEntity>
}