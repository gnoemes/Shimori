package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.AnimeDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore
import kotlinx.datetime.Instant

class AnimeRepository(
    private val dao: AnimeDao,
    private val tracksLastRequest: AnimeWithStatusLastRequestStore,
    private val titleLastRequest: AnimeDetailsLastRequestStore,
    private val titleRolesLastRequest: AnimeRolesLastRequestStore,
) {

    suspend fun queryById(id: Long) = dao.queryById(id)
    fun observeById(id: Long) = dao.observeById(id)

    fun paging(
        status: TrackStatus,
        sort: ListSort
    ) = dao.paging(status, sort)

    suspend fun sync(sourceId: Long, remote: List<Anime>) = dao.sync(sourceId, remote)
    suspend fun sync(sourceId: Long, remote: Anime) = dao.sync(sourceId, arrayListOf(remote))

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