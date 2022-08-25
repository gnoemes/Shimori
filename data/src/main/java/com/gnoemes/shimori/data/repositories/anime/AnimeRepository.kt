package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.AnimeDao
import com.gnoemes.shimori.data.core.database.daos.RateDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.sources.AnimeDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.datetime.Instant

class AnimeRepository(
    private val dao: AnimeDao,
    private val rateDao: RateDao,
    @Shikimori private val source: AnimeDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val ratesLastRequest: AnimeWithStatusLastRequestStore,
    private val titleLastRequest : AnimeDetailsLastRequestStore,
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
        rateDao.syncAll(result.mapNotNull { it.rate }, RateTargetType.ANIME, status)
        ratesLastRequest.updateLastRequest(
            id = status?.priority?.toLong() ?: GroupLastRequestStore.ALL_RATE_STATUSES_ID
        )
    }

    suspend fun update(id : Long) {
        val local = dao.queryById(id)

        if (local != null) {
            val result = source.get(local)

            dao.insertOrUpdate(
                result.entity.copy(
                    id = local.id
                )
            )
            titleLastRequest.updateLastRequest()
        }
    }

    suspend fun needUpdateTitlesWithStatus(
        status: RateStatus?,
        expiry: Instant = instantInPast(minutes = ExpiryConstants.TitlesWithStatus)
    ) = ratesLastRequest.isRequestBefore(
        expiry,
        id = status?.priority?.toLong() ?: GroupLastRequestStore.ALL_RATE_STATUSES_ID
    )

    suspend fun needUpdateTitle(
        id: Long,
        //update details once per day
        expiry: Instant = instantInPast(minutes = ExpiryConstants.TitleDetails)
    ) = titleLastRequest.isRequestBefore(expiry, id = id)
}