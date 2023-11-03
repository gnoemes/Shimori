package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.app.ListPin
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

interface ListPinDao : EntityDao<ListPin> {
    suspend fun pin(targetId: Long, targetType: TrackTargetType, pin: Boolean): Boolean
    suspend fun togglePin(targetId: Long, targetType: TrackTargetType): Boolean

    fun observePinExist(targetId: Long, targetType: TrackTargetType): Flow<Boolean>
    fun observePinsExist(): Flow<Boolean>

    fun paging(
        sort: ListSort
    ): PagingSource<Int, PaginatedEntity>
}