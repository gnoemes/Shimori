package com.gnoemes.shimori.data.repositories.rate

import com.gnoemes.shimori.data.base.database.daos.RateDao
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.base.entities.user.UserShort
import com.gnoemes.shimori.data.base.sources.RateDataSource
import com.gnoemes.shimori.data.base.utils.Shikimori
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository

class RateRepository(
    private val dao: RateDao,
    @Shikimori private val source: RateDataSource,
    private val userRepository: ShikimoriUserRepository
) {
    fun observeById(id: Long) = dao.observeById(id)
    fun observeByShikimoriId(shikimoriId: Long) = dao.observeByShikimoriId(shikimoriId)
    fun observeByTarget(targetId: Long, targetType: RateTargetType) =
        dao.observeByTarget(targetId, targetType)
    fun observeRatesExist() = dao.observeHasRates()

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