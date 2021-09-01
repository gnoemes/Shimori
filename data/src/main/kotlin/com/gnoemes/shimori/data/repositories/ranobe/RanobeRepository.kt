package com.gnoemes.shimori.data.repositories.ranobe

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.instantInPast
import com.gnoemes.shimori.data.repositories.rates.RateStore
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.data_base.sources.RanobeDataSource
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import org.threeten.bp.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RanobeRepository @Inject constructor(
    private val ranobeStore: RanobeStore,
    private val rateStore: RateStore,
    @Shikimori private val ranobeDataSource: RanobeDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequestStore: RanobeWithStatusLastRequestStore
) {

    fun observeById(id : Long) = ranobeStore.observeById(id)
    fun observeByStatusForPaging(status: RateStatus, sort: RateSort) =
        ranobeStore.observeByStatusForPaging(status, sort)

    fun observePinned() = ranobeStore.observePinned()

    suspend fun queryRandomByStatus(status: RateStatus?) = ranobeStore.queryRandomByStatus(status)

    suspend fun updateMyRanobeWithStatus(status: RateStatus?) {
        val userId = userRepository.getMyUserId() ?: return

        val results = ranobeDataSource.getRanobeWithStatus(userId, status)
        if (results is Success && results.data.isNotEmpty()) {
            val ranobe = results.data.filterNot { it.type == null }
            ranobeStore.update(ranobe)
            //Shikimori have only 2 rate target types Anime And Manga
            rateStore.fixRanobeRates(ranobe)
            ratesLastRequestStore.updateLastRequest()
            return
        }
    }

    suspend fun needUpdateRanobeWithStatus(expiry: Instant = instantInPast(minutes = 5)): Boolean {
        return ratesLastRequestStore.isRequestBefore(expiry)
    }

}