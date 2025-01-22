package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

interface ListSortDao : EntityDao<ListSort> {
    suspend fun query(type: TrackTargetType): ListSort?

    fun observe(type: TrackTargetType): Flow<ListSort?>
}