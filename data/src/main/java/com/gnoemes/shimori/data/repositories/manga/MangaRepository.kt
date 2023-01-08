package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.CharacterDao
import com.gnoemes.shimori.data.core.database.daos.MangaDao
import com.gnoemes.shimori.data.core.database.daos.TrackDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.core.sources.MangaDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.datetime.Instant

class MangaRepository(
    private val dao: MangaDao,
    private val trackDao: TrackDao,
    private val characterDao: CharacterDao,
    @Shikimori private val source: MangaDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val tracksLastRequest: MangaWithStatusLastRequestStore,
    private val titleLastRequest: MangaDetailsLastRequestStore,
    private val titleRolesLastRequest: MangaRolesLastRequestStore,
) {
    fun observeById(id: Long) = dao.observeById(id)

    fun paging(
        status: TrackStatus,
        sort: ListSort
    ) = dao.paging(status, sort)

    suspend fun updateMyTitlesByStatus(status: TrackStatus?) {
        val user = userRepository.queryMeShort()
            ?: throw IllegalStateException("User doesn't exist")

        val result = source.getWithStatus(user, status)
        //insert title first
        dao.insertOrUpdate(result.map { it.entity })
        //then sync trackss & assign local target ids
        trackDao.syncAll(result.mapNotNull { it.track }, TrackTargetType.MANGA, status)
        tracksLastRequest.updateLastRequest(
            id = status?.priority?.toLong() ?: GroupLastRequestStore.ALL_TRACK_STATUSES_ID
        )
    }

    suspend fun update(id : Long) {
        val local = dao.queryById(id)

        if (local != null) {
            val result = source.get(local)

            if (result.entity.mangaType == null) return

            dao.insertOrUpdate(
                result.entity.copy(
                    id = local.id
                )
            )
            titleLastRequest.updateLastRequest(id = id)
        }
    }

    suspend fun updateRoles(id: Long) {
        val local = dao.queryById(id)

        if (local != null) {
            val result = source.roles(local)

            characterDao.sync(
                id,
                TrackTargetType.MANGA,
                result.characters
            )

            titleRolesLastRequest.updateLastRequest(id = id)
        }
    }

    suspend fun needUpdateTitlesWithStatus(
        status: TrackStatus?,
        expiry: Instant = instantInPast(minutes = ExpiryConstants.TitlesWithStatus)
    ) = tracksLastRequest.isRequestBefore(
        expiry,
        id = status?.priority?.toLong() ?: GroupLastRequestStore.ALL_TRACK_STATUSES_ID
    )

    suspend fun needUpdateTitle(
        id: Long,
        //update details once per day
        expiry: Instant = instantInPast(minutes = ExpiryConstants.TitleDetails)
    ) = titleLastRequest.isRequestBefore(expiry, id = id)

    suspend fun needUpdateTitleRoles(
        id: Long,
        //update details once per week
        expiry: Instant = instantInPast(minutes = ExpiryConstants.TitleRoles)
    ) = titleRolesLastRequest.isRequestBefore(expiry, id = id)
}