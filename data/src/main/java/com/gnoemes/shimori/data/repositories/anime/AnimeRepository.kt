package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.base.database.daos.AnimeDao
import com.gnoemes.shimori.data.base.entities.rate.RateSort
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.sources.AnimeDataSource
import com.gnoemes.shimori.data.base.utils.Shikimori
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.datetime.Instant

class AnimeRepository(
    private val dao: AnimeDao,
    @Shikimori private val source: AnimeDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequest: AnimeWithStatusLastRequestStore
) {
    fun observeById(id: Long) = dao.observeById(id)
    fun observeByStatus(status: RateStatus, sort: RateSort) = dao.observeByStatus(status, sort)

    suspend fun updateMyTitlesByStatus(status: RateStatus?) {
        val user = userRepository.queryMeShort()
            ?: throw IllegalStateException("User doesn't exist")

        val result = source.getWithStatus(user, status)
        dao.insertOrUpdate(result)
        ratesLastRequest.updateLastRequest(id = status?.priority?.toLong() ?: 0)
    }

    suspend fun needUpdateTitlesWithStatus(
        status: RateStatus?,
        expiry: Instant = instantInPast(minutes = 5)
    ) = ratesLastRequest.isRequestBefore(expiry, id = status?.priority?.toLong() ?: 0)

}