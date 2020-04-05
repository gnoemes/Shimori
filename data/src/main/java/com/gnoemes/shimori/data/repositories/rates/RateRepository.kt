package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.asyncOrAwait
import com.gnoemes.shimori.data.repositories.ratesort.RateSortStore
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.data_base.sources.RateDataSource
import com.gnoemes.shimori.model.app.RateSort
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RateRepository @Inject constructor(
    private val rateStore: RateStore,
    private val rateSortStore: RateSortStore,
    @Shikimori private val rateSource: RateDataSource,
    private val userRepository: UserRepository
) {

    fun observeRate(shikimoriId: Long) = rateStore.observeRate(shikimoriId)

    fun observeRates(type: RateTargetType) = rateStore.observeRates(type)

    fun observeRateSort(type: RateTargetType, status: RateStatus) =
        rateSortStore.observeSort(type, status)

    suspend fun createOrUpdate(rate: Rate) {
        asyncOrAwait("crate_or_update_rate") {
            val id = rateStore.createOrUpdate(rate)
            //TODO plan to upload
            val localRate = rateStore.dao.queryWithId(id)
            if (localRate != null) {
                val result =
                    if (localRate.shikimoriId == null) rateSource.createRate(localRate)
                    else rateSource.updateRate(localRate)

                if (result is Success) {
                    rateStore.createOrUpdate(result.data)
                }
            }
        }
    }

    suspend fun getRates(userId: Long) {
        asyncOrAwait("get_rates") {
            val results = rateSource.getRates(userId)
            if (results is Success && results.data.isNotEmpty()) {
                rateStore.updateRates(results.data)
                return@asyncOrAwait
            }
        }
    }

    suspend fun syncRates() = asyncOrAwait("sync_rates") {
        val userId = userRepository.getMyUserId()

        //TODO pending rates

        if (userId != null) {
            diffAndUpdateRates(userId)
        }
    }

    suspend fun updateRateSort(sort: RateSort) = rateSortStore.updateSort(sort)

    //TODO
    suspend fun needSyncRates(expiry: DateTime): Boolean {
        return false
    }

    private suspend fun diffAndUpdateRates(userId: Long) {
        val remote = rateSource.getRates(userId)
        if (remote is Success && remote.data.isNotEmpty()) {
            rateStore.syncAll(remote.data)
        }
    }

}