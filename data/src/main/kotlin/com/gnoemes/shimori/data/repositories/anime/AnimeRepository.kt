package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(
    private val animeStore: AnimeStore,
    @Shikimori private val animeDataSource: AnimeDataSource,
    private val userRepository: ShikimoriUserRepository
) {

    fun observeByStatusForPaging(status: RateStatus?, sort: RateSort) = animeStore.observeByStatusForPaging(status, sort)


    suspend fun queryAnimesWithStatus(status: RateStatus?) = animeStore.queryAnimesWithStatus(status)

    suspend fun updateMyAnimeWithStatus(status: RateStatus?) {
        val userId = userRepository.getMyUserId() ?: return

        val results = animeDataSource.getAnimeWithStatus(userId, status)
        if (results is Success && results.data.isNotEmpty()) {
            animeStore.updateAnimes(results.data)
            return
        }
    }

}