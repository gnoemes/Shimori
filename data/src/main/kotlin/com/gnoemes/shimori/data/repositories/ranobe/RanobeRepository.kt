package com.gnoemes.shimori.data.repositories.ranobe

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.instantInPast
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
    @Shikimori private val ranobeDataSource: RanobeDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequestStore: RanobeWithStatusLastRequestStore
) {

    fun observeByStatusForPaging(status: RateStatus, sort: RateSort) =
        ranobeStore.observeByStatusForPaging(status, sort)

    suspend fun queryRandomByStatus(status: RateStatus?) = ranobeStore.queryRandomByStatus(status)

    suspend fun updateMyRanobeWithStatus(status: RateStatus?) {
        val userId = userRepository.getMyUserId() ?: return

        val results = ranobeDataSource.getRanobeWithStatus(userId, status)
        if (results is Success && results.data.isNotEmpty()) {
            val ranobe = results.data.filterNot { it.type == null }
            ranobeStore.update(ranobe)
            ratesLastRequestStore.updateLastRequest()
            return
        }
    }

    suspend fun needUpdateRanobeWithStatus(expiry: Instant = instantInPast(hours = 2)): Boolean {
        return ratesLastRequestStore.isRequestBefore(expiry)
    }

}