package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.MangaDao
import com.gnoemes.shimori.data.core.database.daos.RateDao
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.sources.MangaDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.datetime.Instant

class MangaRepository(
    private val dao: MangaDao,
    private val rateDao: RateDao,
    @Shikimori private val source: MangaDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequest: MangaWithStatusLastRequestStore
) {
    fun observeById(id: Long) = dao.observeById(id)

    fun paging(
        status: RateStatus,
        sort: RateSort
    ) = dao.paging(status, sort)

    suspend fun updateMyTitlesByStatus(status: RateStatus?) {
        val user = userRepository.queryMeShort()
            ?: throw IllegalStateException("User doesn't exist")

        val result = source.getWithStatus(user, status)
        //insert title first
        dao.insertOrUpdate(result.map { it.entity })
        //then sync rates & assign local target ids
        rateDao.syncAll(result.mapNotNull { it.rate }, RateTargetType.MANGA, status)
        ratesLastRequest.updateLastRequest(
            id = status?.priority?.toLong() ?: GroupLastRequestStore.ALL_RATE_STATUSES_ID
        )
    }

    suspend fun needUpdateTitlesWithStatus(
        status: RateStatus?,
        expiry: Instant = instantInPast(minutes = 5)
    ) = ratesLastRequest.isRequestBefore(expiry, id = status?.priority?.toLong() ?: 0)

}