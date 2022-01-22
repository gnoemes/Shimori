package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.data.repositories.ratesort.RateSortStore
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.data_base.sources.RateDataSource
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.model.user.UserShort
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
    fun observeByTarget(targetId: Long, targetType: RateTargetType) =
        rateStore.observeByTarget(targetId, targetType)

    fun observeRateSort(type: ListType) = rateSortStore.observeSort(type)
    fun observeListsPages(type: RateTargetType) = rateStore.observeListsPages(type)
    fun observeHasRates() = rateStore.observeHasRates()

    suspend fun createOrUpdate(rate: Rate) {
        val id = rateStore.createOrUpdate(rate)
        //TODO plan to upload?
        val localRate = rateStore.dao.queryById(id)
        if (localRate != null) {
            val result =
                if (localRate.shikimoriId == null) rateSource.createRate(localRate)
                else rateSource.updateRate(localRate)

            rateStore.createOrUpdate(result)
        }
    }

    suspend fun deleteRate(id: Long) {
        val localRate = rateStore.dao.queryById(id)
        //TODO plan to delete?
        localRate?.shikimoriId?.let {
            val result = rateSource.deleteRate(it)

            rateStore.dao.deleteWithId(localRate.id)
        }
    }

    suspend fun syncRates() {
        val user = userRepository.getMyUserShort()

        //TODO pending rates

        if (user != null) {
            diffAndUpdateRates(user)
        }
    }

    suspend fun updateRateSort(sort: RateSort) = rateSortStore.updateSort(sort)


    private suspend fun diffAndUpdateRates(user: UserShort) {
        val remote = rateSource.getRates(user)
        rateStore.syncAll(remote)
    }

}