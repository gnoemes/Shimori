package com.gnoemes.shimori.data.repositories.ranobe

import com.gnoemes.shimori.base.di.Shikimori
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

    fun observeById(id: Long) = ranobeStore.observeById(id)
    fun observeByStatusForPaging(status: RateStatus, sort: RateSort) =
        ranobeStore.observeByStatusForPaging(status, sort)

    fun observePinned(sort: RateSort) = ranobeStore.observePinned(sort)

    suspend fun queryRandomByStatus(status: RateStatus?) = ranobeStore.queryRandomByStatus(status)

    suspend fun updateMyRanobeWithStatus(status: RateStatus?) {
        val user = userRepository.getMyUserShort() ?: return

        val result = ranobeDataSource.getRanobeWithStatus(user, status)
        val ranobe = result.filterNot { it.type == null }
        ranobeStore.update(ranobe)
        //Shikimori have only 2 rate target types Anime And Manga
        rateStore.fixRanobeRates(ranobe)
        ratesLastRequestStore.updateLastRequest(id = status?.priority?.toLong() ?: 0)
        return
    }

    suspend fun needUpdateRanobeWithStatus(
        status: RateStatus?,
        expiry: Instant = instantInPast(minutes = 5)
    ) = ratesLastRequestStore.isRequestBefore(expiry, status?.priority?.toLong() ?: 0)


}