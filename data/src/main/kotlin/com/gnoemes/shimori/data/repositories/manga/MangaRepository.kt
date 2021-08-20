package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.instantInPast
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.data_base.sources.MangaDataSource
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import org.threeten.bp.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(
    private val mangaStore: MangaStore,
    @Shikimori private val mangaDataSource: MangaDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequestStore: MangaWithStatusLastRequestStore
) {

    fun observeByStatusForPaging(status : RateStatus, sort: RateSort) = mangaStore.observeByStatusForPaging(status, sort)

    suspend fun updateMyMangaWithStatus(status: RateStatus?) {
        val userId = userRepository.getMyUserId() ?: return

        val results = mangaDataSource.getMangaWithStatus(userId, status)
        if (results is Success && results.data.isNotEmpty()) {
            mangaStore.update(results.data)
            ratesLastRequestStore.updateLastRequest()
            return
        }
    }

    suspend fun needUpdateMangaWithStatus(expiry: Instant = instantInPast(hours = 2)): Boolean {
        return ratesLastRequestStore.isRequestBefore(expiry)
    }

}