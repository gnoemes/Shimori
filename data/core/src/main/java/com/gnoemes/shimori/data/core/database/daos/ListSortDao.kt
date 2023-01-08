package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListType
import kotlinx.coroutines.flow.Flow

abstract class ListSortDao : EntityDao<ListSort>() {
    abstract suspend fun query(type: ListType): ListSort?

    abstract fun observe(type: ListType): Flow<ListSort?>
}