package com.gnoemes.shimori.data.repositories.ranobe

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.RanobeDao
import com.gnoemes.shimori.data.core.database.daos.RateDao
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.sources.RanobeDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.datetime.Instant

class RanobeRepository(
    private val dao: RanobeDao,
    private val rateDao: RateDao,
    @Shikimori private val source: RanobeDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequest: RanobeWithStatusLastRequestStore
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
        rateDao.syncAll(result.mapNotNull { it.rate }, RateTargetType.RANOBE, status)
        ratesLastRequest.updateLastRequest(
            id = status?.priority?.toLong() ?: GroupLastRequestStore.ALL_RATE_STATUSES_ID
        )
    }

    suspend fun needUpdateTitlesWithStatus(
        status: RateStatus?,
        expiry: Instant = instantInPast(minutes = 15)
    ) = ratesLastRequest.isRequestBefore(expiry, id = status?.priority?.toLong() ?: 0)

}