package com.gnoemes.shimori.data.repositories.ratesort

import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.daos.RateSortDao
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject

class RateSortStore @Inject constructor(
    private val inserter: EntityInserter,
    private val runner: DatabaseTransactionRunner,
    private val dao: RateSortDao
) {

    fun observeSort(type: RateTargetType, status: RateStatus) =
        dao.observeSort(type, status)

    suspend fun updateSort(sort: RateSort) {
        if (sort.type == null || sort.status == null) return

        val saved = dao.querySort(sort.type!!, sort.status!!)
        val new = sort.copy(id = saved?.id ?: 0)

        inserter.insertOrUpdate(dao, new)
    }
}