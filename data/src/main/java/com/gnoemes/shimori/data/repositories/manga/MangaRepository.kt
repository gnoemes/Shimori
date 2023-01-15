package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.CharacterDao
import com.gnoemes.shimori.data.core.database.daos.MangaDao
import com.gnoemes.shimori.data.core.database.daos.TrackDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore
import com.gnoemes.shimori.data.repositories.user.UserRepository
import kotlinx.datetime.Instant

class MangaRepository(
    private val dao: MangaDao,
    private val trackDao: TrackDao,
    private val characterDao: CharacterDao,
    private val userRepository: UserRepository,
    private val tracksLastRequest: MangaWithStatusLastRequestStore,
    private val titleLastRequest: MangaDetailsLastRequestStore,
    private val titleRolesLastRequest: MangaRolesLastRequestStore,
) {
    suspend fun queryById(id: Long) = dao.queryById(id)
    fun observeById(id: Long) = dao.observeById(id)

    fun paging(
        status: TrackStatus,
        sort: ListSort
    ) = dao.paging(status, sort)

    suspend fun sync(sourceId: Long, remote: List<Manga>) = dao.sync(sourceId, remote)
    suspend fun sync(sourceId: Long, remote: Manga) = dao.sync(sourceId, arrayListOf(remote))

    suspend fun titleUpdated(id: Long) = titleLastRequest.updateLastRequest(id = id)
    suspend fun rolesUpdated(id: Long) = titleRolesLastRequest.updateLastRequest(id = id)
    suspend fun statusUpdated(status: TrackStatus?) = tracksLastRequest.updateLastRequest(
        id = status?.priority?.toLong() ?: GroupLastRequestStore.ALL_TRACK_STATUSES_ID
    )

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