package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.instantInPast
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import org.threeten.bp.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(
    private val animeStore: AnimeStore,
    @Shikimori private val animeDataSource: AnimeDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequestStore: AnimeWithStatusLastRequestStore
) {

    fun observeByStatusForPaging(status: RateStatus, sort: RateSort) = animeStore.observeByStatusForPaging(status, sort)

    suspend fun queryByStatus(status: RateStatus?) = animeStore.queryByStatus(status)

    suspend fun queryRandomByStatus(status: RateStatus?) = animeStore.queryRandomByStatus(status)

    suspend fun updateMyAnimeWithStatus(status: RateStatus?) {
        val userId = userRepository.getMyUserId() ?: return

        val results = animeDataSource.getAnimeWithStatus(userId, status)
        if (results is Success && results.data.isNotEmpty()) {
            animeStore.update(results.data)
            ratesLastRequestStore.updateLastRequest()
            return
        }
    }

    suspend fun needUpdateAnimeWithStatus(expiry: Instant = instantInPast(minutes = 5)): Boolean {
        return ratesLastRequestStore.isRequestBefore(expiry)
    }

}