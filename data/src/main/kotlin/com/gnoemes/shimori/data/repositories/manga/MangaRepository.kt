package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.base.di.Shikimori
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

    fun observeById(id: Long) = mangaStore.observeById(id)
    fun observeByStatusForPaging(status: RateStatus, sort: RateSort) =
        mangaStore.observeByStatusForPaging(status, sort)

    fun observePinned(sort: RateSort) = mangaStore.observePinned(sort)

    suspend fun queryRandomByStatus(status: RateStatus?) = mangaStore.queryRandomByStatus(status)

    suspend fun updateMyMangaWithStatus(status: RateStatus?) {
        val user = userRepository.getMyUserShort() ?: return

        val result = mangaDataSource.getMangaWithStatus(user, status)
        val mangas = result.filterNot { it.type == null }
        mangaStore.update(mangas)
        ratesLastRequestStore.updateLastRequest(id = status?.priority?.toLong() ?: 0)
        return
    }

    suspend fun needUpdateMangaWithStatus(
        status: RateStatus?,
        expiry: Instant = instantInPast(minutes = 5)
    ) = ratesLastRequestStore.isRequestBefore(expiry, status?.priority?.toLong() ?: 0)


}