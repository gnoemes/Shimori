package com.gnoemes.shimori.data.repositories.rate

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.base.core.tasks.RateTasks
import com.gnoemes.shimori.data.core.database.daos.RateDao
import com.gnoemes.shimori.data.core.database.daos.RateSortDao
import com.gnoemes.shimori.data.core.database.daos.RateToSyncDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.app.SyncAction
import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.rate.*
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.sources.RateDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class RateRepository(
    private val dao: RateDao,
    private val syncDao: RateToSyncDao,
    private val rateSortDao: RateSortDao,
    @Shikimori private val source: RateDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val rateTasks: RateTasks,
    private val syncPendingRatesLastRequest: SyncPendingRatesLastRequestStore
) {

    suspend fun queryById(id: Long) = dao.queryById(id)

    suspend fun querySyncPendingRates() = syncDao.queryAll()

    fun observeRatesExist() = dao.observeHasRates()
    fun observeRateSort(type: ListType): Flow<RateSort?> = rateSortDao.observe(type)
    fun observeExistedStatuses(type: RateTargetType) = dao.observeExistedStatuses(type)

    suspend fun createOrUpdate(rate: Rate) {
        dao.insertOrUpdate(
            rate.copy(
                dateUpdated = Clock.System.now(),
                dateCreated = rate.dateCreated ?: Clock.System.now()
            )
        )

        val local = dao.queryById(rate.id)
        if (local != null) {
            val action =
                if (rate.id == 0L || !local.hasShikimoriId) SyncAction.CREATE
                else SyncAction.UPDATE

            updateSyncPending(local, action)
        }
    }

    suspend fun createOrUpdate(rateToSync: RateToSync) {
        syncDao.insertOrUpdate(rateToSync)
    }

    suspend fun createOrUpdateOnTarget(local: Rate, target: SyncTarget) {
        val result = if (target.api == SyncApi.Shikimori) {
            if (!local.hasShikimoriId) source.createRate(local)
            else source.updateRate(local)
        } else throw IllegalArgumentException("${target.api} is not supported yet")


        //sync with local
        dao.insertOrUpdate(
            result.copy(
                id = local.id,
                targetId = local.targetId,
                targetType = local.targetType
            )
        )
    }


    suspend fun createOrUpdate(rateSort: RateSort) {
        rateSortDao.insertOrUpdate(rateSort)
    }

    suspend fun delete(id: Long) {
        val local = dao.queryById(id)

        local?.let {
            dao.delete(it)
            updateSyncPending(it, SyncAction.DELETE)
        }
    }

    suspend fun delete(rateToSync: RateToSync) {
        syncDao.delete(rateToSync)
    }

    suspend fun deleteFromTarget(target: SyncTarget) {
        source.deleteRate(target.id)
    }

    suspend fun sync() {
        val user = userRepository.queryMeShort()

        if (user != null) {
            diffAndUpdateRates(user)
        }
    }

    private suspend fun diffAndUpdateRates(user: UserShort) {
        val remote = source.getRates(user)
        dao.syncAll(remote)
    }

    private suspend fun updateSyncPending(rate: Rate, action: SyncAction) {
        val local = syncDao.queryByRateId(rate.id)

        val rateToSync: RateToSync
        if (local != null) {
            if (local.action == SyncAction.CREATE) {
                if (action == SyncAction.DELETE) {
                    //delete from pending sync if not created on external apis
                    syncDao.delete(local)
                    return
                }

                //do nothing if rate is not created yet
                if (action == SyncAction.UPDATE) return
            }

            rateToSync = local.copy(
                action = action
            )
        } else {
            rateToSync = RateToSync(
                rateId = rate.id,
                targets = listOf(
                    SyncTarget(
                        SyncApi.Shikimori,
                        rate.shikimoriId
                    )
                ),
                action = action
            )
        }

        syncDao.insertOrUpdate(rateToSync)

        if (needSyncPendingRates()) {
            rateTasks.syncPendingRates()
        }
    }

    suspend fun needSyncPendingRates(
        expiry: Instant = instantInPast(minutes = ExpiryConstants.SyncPendingRates)
    ) = syncPendingRatesLastRequest.isRequestBefore(expiry)
}