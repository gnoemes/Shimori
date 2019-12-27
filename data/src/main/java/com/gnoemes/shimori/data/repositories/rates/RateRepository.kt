package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.asyncOrAwait
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.data_base.sources.RateDataSource
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RateRepository @Inject constructor(
    private val rateStore: RateStore,
    @Shikimori private val rateSource: RateDataSource,
    private val userRepository: UserRepository
) {

    fun observeAnimeRates() = rateStore.observeRates()

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

    //TODO
    suspend fun needSyncRates(expiry : DateTime) : Boolean {
        return false
    }

    private suspend fun diffAndUpdateRates(userId: Long) {
        val remote = rateSource.getRates(userId)
        if (remote is Success && remote.data.isNotEmpty()) {
            rateStore.syncAll(remote.data)
        }
    }

}