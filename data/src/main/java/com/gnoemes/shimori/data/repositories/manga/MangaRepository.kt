package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.base.database.daos.MangaDao
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.sources.MangaDataSource
import com.gnoemes.shimori.data.base.utils.Shikimori
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.datetime.Instant

class MangaRepository(
    private val dao: MangaDao,
    @Shikimori private val source: MangaDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequest: MangaWithStatusLastRequestStore
) {
    fun observeById(id: Long) = dao.observeById(id)

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