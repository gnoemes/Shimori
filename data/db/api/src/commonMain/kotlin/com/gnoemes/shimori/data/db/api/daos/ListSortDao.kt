package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListType
import kotlinx.coroutines.flow.Flow

interface ListSortDao : EntityDao<ListSort> {
    suspend fun query(type: ListType): ListSort?

    fun observe(type: ListType): Flow<ListSort?>
}