package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.daos.RateDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RateStore @Inject constructor(
    private val inserter: EntityInserter,
    private val runner: DatabaseTransactionRunner,
    val dao: RateDao
) {
    private val syncer = syncerForEntity(
            dao,
            { it.shikimoriId },
            { entity, id -> entity.copy(id = id ?: 0) }
    )

    fun observeRate(shikimoriId: Long) = dao.observeRate(shikimoriId)

    fun observeRates(target: RateTargetType): Flow<List<Rate>> =
        when (target) {
            RateTargetType.ANIME -> dao.observeAnimeRates()
            else -> throw IllegalArgumentException("$target is not supported yet")
        }

    fun observeListsPages(target: RateTargetType): Flow<List<RateStatus>> {
        return combine(
                *RateStatus.listPagesOrder.map { status ->
                    dao.observePageExist(target, status)
                        .map { count -> status to (count > 0) }
                }
                    .toTypedArray()
        ) { statuses ->
            statuses
                .filter { it.second }
                .map { it.first }
        }
    }

    suspend fun createOrUpdate(rate: Rate): Long {
        return if (rate.shikimoriId == null) dao.insert(rate)
        else {
            dao.update(rate)
            rate.id
        }
    }

    suspend fun updateRates(rates: List<Rate>) {
        val localRates = dao.queryWithShikimoriIds(rates.mapNotNull { it.shikimoriId })


        val merged = rates.map { newRate ->
            val local = localRates.find { it.shikimoriId == newRate.shikimoriId }

            newRate.copy(id = local?.id ?: 0)
        }

        inserter.insertOrUpdate(dao, merged)
    }

    suspend fun syncAll(data: List<Rate>) = runner {
        syncer.sync(dao.queryAll(), data)
    }
}