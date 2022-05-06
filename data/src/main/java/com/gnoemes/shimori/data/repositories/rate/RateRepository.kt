package com.gnoemes.shimori.data.repositories.rate

import com.gnoemes.shimori.data.core.database.daos.RateDao
import com.gnoemes.shimori.data.core.database.daos.RateSortDao
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.sources.RateDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.coroutines.flow.Flow

class RateRepository(
    private val dao: RateDao,
    private val rateSortDao: RateSortDao,
    @Shikimori private val source: RateDataSource,
    private val userRepository: ShikimoriUserRepository
) {
    fun observeById(id: Long) = dao.observeById(id)
    fun observeByShikimoriId(shikimoriId: Long) = dao.observeByShikimoriId(shikimoriId)
    fun observeByTarget(targetId: Long, targetType: RateTargetType) =
        dao.observeByTarget(targetId, targetType)

    fun observeRatesExist() = dao.observeHasRates()
    fun observeRateSort(type: ListType): Flow<RateSort?> = rateSortDao.observe(type)

    suspend fun createOrUpdate(rate: Rate) {
        dao.insertOrUpdate(rate)

        val local = dao.queryById(rate.id)
        if (local != null) {
            val result =
                if (!local.hasShikimoriId) source.createRate(local)
                else source.updateRate(local)

            dao.insertOrUpdate(result)
        }
    }

    suspend fun createOrUpdate(rateSort: RateSort) {
        rateSortDao.insertOrUpdate(rateSort)
    }

    suspend fun delete(id: Long) {
        val local = dao.queryById(id)

        local?.let {
            source.deleteRate(it.shikimoriId)
            dao.deleteEntity(it)
        }
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


}