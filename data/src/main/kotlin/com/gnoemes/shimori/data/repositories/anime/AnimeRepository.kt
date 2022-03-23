package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.di.Shikimori
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
    fun observeById(id: Long) = animeStore.observeById(id)
    fun observeByStatusForPaging(status: RateStatus, sort: RateSort) =
        animeStore.observeByStatusForPaging(status, sort)

    fun observePinned(sort: RateSort) = animeStore.observePinned(sort)

    suspend fun queryByStatus(status: RateStatus?) = animeStore.queryByStatus(status)

    suspend fun queryRandomByStatus(status: RateStatus?) = animeStore.queryRandomByStatus(status)

    suspend fun updateMyAnimeWithStatus(status: RateStatus?) {
        val user = userRepository.getMyUserShort() ?: return

        val result = animeDataSource.getAnimeWithStatus(user, status)
        animeStore.update(result)
        ratesLastRequestStore.updateLastRequest(id = status?.priority?.toLong() ?: 0)
        return
    }

    suspend fun needUpdateAnimeWithStatus(
        status: RateStatus?,
        expiry: Instant = instantInPast(minutes = 5)
    ) = ratesLastRequestStore.isRequestBefore(expiry, status?.priority?.toLong() ?: 0)

}