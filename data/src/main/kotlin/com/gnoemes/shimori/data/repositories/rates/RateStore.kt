package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.daos.RateDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.ranobe.Ranobe
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
            { remote, local ->
                if (local == null || local.targetType != RateTargetType.RANOBE) {
                    remote.copy(id = local?.id ?: 0)
                } else {
                    //fix shikimori rates (supports only Anime And Manga)
                    remote.copy(
                            id = local.id,
                            mangaId = null,
                            ranobeId = local.ranobeId,
                            targetType = RateTargetType.RANOBE
                    )
                }
            }
    )

    fun observeById(id: Long) = dao.observeById(id)
    fun observeByShikimoriId(shikimoriId: Long) = dao.observeByShikimoriId(shikimoriId)
    fun observeByTarget(targetId: Long, targetType: RateTargetType) = when (targetType) {
        RateTargetType.ANIME -> dao.observeByAnimeId(targetId)
        RateTargetType.MANGA -> dao.observeByMangaId(targetId)
        RateTargetType.RANOBE -> dao.observeByRanobeId(targetId)
    }

    fun observeHasRates() = dao.observeRateCount().map { it > 0 }

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

    suspend fun fixRanobeRates(ranobe: List<Ranobe>) {
        val localRates = dao.queryByMangaIds(ranobe.mapNotNull { it.shikimoriId })

        val fixedRates = localRates.map { localRate ->
            val id = localRate.mangaId

            localRate.copy(mangaId = null, ranobeId = id, targetType = RateTargetType.RANOBE)
        }

        inserter.insertOrUpdate(dao, fixedRates)
    }
}