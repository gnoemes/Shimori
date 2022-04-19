package com.gnoemes.shimori.data.repositories.rate

import com.gnoemes.shimori.data.base.database.daos.RateDao
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.base.sources.RateDataSource
import com.gnoemes.shimori.data.base.utils.Shikimori

class RateRepository(
    private val rateDao: RateDao, @Shikimori private val rateSource: RateDataSource
) {
    fun observeById(id: Long) = rateDao.observeById(id)
    fun observeByShikimoriId(shikimoriId: Long) = rateDao.observeByShikimoriId(shikimoriId)
    fun observeByTarget(targetId: Long, targetType: RateTargetType) =
        rateDao.observeByTarget(targetId, targetType)

    suspend fun createOrUpdate(rate: Rate) {
        rateDao.insertOrUpdate(rate)

        val local = rateDao.queryById(rate.id)
        if (local != null) {
            val result =
                if (!local.hasShikimoriId) rateSource.createRate(local)
                else rateSource.updateRate(local)

            rateDao.insertOrUpdate(result)
        }
    }

    suspend fun delete(id: Long) {
        val local = rateDao.queryById(id)

        local?.let {
            rateSource.deleteRate(it.shikimoriId)
            rateDao.deleteEntity(it)
        }
    }

    suspend fun sync() {

    }

}