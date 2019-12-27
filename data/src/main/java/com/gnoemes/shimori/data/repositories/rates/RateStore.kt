package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.daos.RateDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.rate.Rate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RateStore @Inject constructor(
    private val inserter: EntityInserter,
    private val runner: DatabaseTransactionRunner,
    private val dao: RateDao
) {

    private val syncer = syncerForEntity(
            dao,
            { it.shikimoriId },
            { entity, id -> entity.copy(id = id ?: 0) }
    )

    fun observeRates(): Flow<List<Rate>> = dao.observeAnimeRates()

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