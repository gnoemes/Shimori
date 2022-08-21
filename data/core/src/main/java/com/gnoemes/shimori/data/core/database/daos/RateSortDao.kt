package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import kotlinx.coroutines.flow.Flow

abstract class RateSortDao : EntityDao<RateSort>() {
    abstract suspend fun query(type: ListType): RateSort?

    abstract fun observe(type: ListType): Flow<RateSort?>
}