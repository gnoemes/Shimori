package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.data.repositories.ratesort.RateSortStore
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.data_base.sources.RateDataSource
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RateRepository @Inject constructor(
    private val rateStore: RateStore,
    private val rateSortStore: RateSortStore,
    @Shikimori private val rateSource: RateDataSource,
    private val userRepository: ShikimoriUserRepository
) {

    fun observeById(id: Long) = rateStore.observeById(id)
    fun observeByShikimoriId(shikimoriId: Long) = rateStore.observeByShikimoriId(shikimoriId)
    fun observeByTarget(targetId: Long, targetType: RateTargetType) = rateStore.observeByTarget(targetId, targetType)
    fun observeRateSort(type: ListType) = rateSortStore.observeSort(type)
    fun observeListsPages(type: RateTargetType) = rateStore.observeListsPages(type)

    suspend fun createOrUpdate(rate: Rate) {
        val id = rateStore.createOrUpdate(rate)
        //TODO plan to upload?
        val localRate = rateStore.dao.queryById(id)
        if (localRate != null) {
            val result =
                if (localRate.shikimoriId == null) rateSource.createRate(localRate)
                else rateSource.updateRate(localRate)

            if (result is Success) {
                rateStore.createOrUpdate(result.data)
            }
        }
    }

    suspend fun deleteRate(id: Long) {
        val localRate = rateStore.dao.queryById(id)
        //TODO plan to delete?
        localRate?.shikimoriId?.let {
            val result = rateSource.deleteRate(it)

            if (result is Success) {
                rateStore.dao.deleteWithId(localRate.id)
            }
        }
    }

    suspend fun getRates(userId: Long) {
        val results = rateSource.getRates(userId)
        if (results is Success && results.data.isNotEmpty()) {
            rateStore.updateRates(results.data)
            return
        }
    }

    suspend fun syncRates() {
        val userId = userRepository.getMyUserId()

        //TODO pending rates

        if (userId != null) {
            diffAndUpdateRates(userId)
        }
    }

    suspend fun updateRateSort(sort: RateSort) = rateSortStore.updateSort(sort)


    private suspend fun diffAndUpdateRates(userId: Long) {
        val remote = rateSource.getRates(userId)
        if (remote is Success && remote.data.isNotEmpty()) {
            rateStore.syncAll(remote.data)
        }
    }

}