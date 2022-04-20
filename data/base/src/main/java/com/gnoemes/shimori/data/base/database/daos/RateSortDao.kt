package com.gnoemes.shimori.data.base.database.daos

import com.gnoemes.shimori.data.base.entities.rate.ListType
import com.gnoemes.shimori.data.base.entities.rate.RateSort
import kotlinx.coroutines.flow.Flow

abstract class RateSortDao : EntityDao<RateSort>() {
    abstract suspend fun query(type: ListType): RateSort?

    abstract fun observe(type: ListType): Flow<RateSort?>
}