package com.gnoemes.shimori.data.repositories.track

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.base.core.tasks.TrackTasks
import com.gnoemes.shimori.data.core.database.daos.ListSortDao
import com.gnoemes.shimori.data.core.database.daos.TrackDao
import com.gnoemes.shimori.data.core.database.daos.TrackToSyncDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.app.SyncAction
import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.track.*
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.sources.TrackDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class TrackRepository(
    private val dao: TrackDao,
    private val syncDao: TrackToSyncDao,
    private val listSortDao: ListSortDao,
    @Shikimori private val source: TrackDataSource,
    private val userRepository: ShikimoriUserRepository,
    private val trackTasks: TrackTasks,
    private val syncPendingTracksLastRequest: SyncPendingTracksLastRequestStore
) {

    suspend fun queryById(id: Long) = dao.queryById(id)

    suspend fun querySyncPendingTracks() = syncDao.queryAll()

    fun observeTracksExist() = dao.observeHasTracks()
    fun observeListSort(type: ListType): Flow<ListSort?> = listSortDao.observe(type)
    fun observeExistedStatuses(type: TrackTargetType) = dao.observeExistedStatuses(type)

    suspend fun createOrUpdate(track: Track) {
        dao.insertOrUpdate(
            track.copy(
                dateUpdated = Clock.System.now(),
                dateCreated = track.dateCreated ?: Clock.System.now()
            )
        )

        val local = dao.queryById(track.id)
        if (local != null) {
            val action =
                if (track.id == 0L || !local.hasShikimoriId) SyncAction.CREATE
                else SyncAction.UPDATE

            updateSyncPending(local, action)
        }
    }

    suspend fun createOrUpdate(trackToSync: TrackToSync) {
        syncDao.insertOrUpdate(trackToSync)
    }

    suspend fun createOrUpdateOnTarget(local: Track, target: SyncTarget) {
        val result = if (target.api == SyncApi.Shikimori) {
            if (!local.hasShikimoriId) source.create(local)
            else source.update(local)
        } else throw IllegalArgumentException("${target.api} is not supported yet")


        //sync with local
        dao.insertOrUpdate(
            result.copy(
                id = local.id,
                targetId = local.targetId,
                targetType = local.targetType
            )
        )
    }


    suspend fun createOrUpdate(listSort: ListSort) {
        listSortDao.insertOrUpdate(listSort)
    }

    suspend fun delete(id: Long) {
        val local = dao.queryById(id)

        local?.let {
            dao.delete(it)
            updateSyncPending(it, SyncAction.DELETE)
        }
    }

    suspend fun delete(trackToSync: TrackToSync) {
        syncDao.delete(trackToSync)
    }

    suspend fun deleteFromTarget(target: SyncTarget) {
        source.delete(target.id)
    }

    suspend fun sync() {
        val user = userRepository.queryMeShort()

        if (user != null) {
            diffAndUpdateTracks(user)
        }
    }

    private suspend fun diffAndUpdateTracks(user: UserShort) {
        val remote = source.getList(user)
        dao.syncAll(remote)
    }

    private suspend fun updateSyncPending(track: Track, action: SyncAction) {
        val local = syncDao.queryByTrackId(track.id)

        val trackToSync: TrackToSync
        if (local != null) {
            if (local.action == SyncAction.CREATE) {
                if (action == SyncAction.DELETE) {
                    //delete from pending sync if not created on external apis
                    syncDao.delete(local)
                    return
                }

                //do nothing if track is not created yet
                if (action == SyncAction.UPDATE) return
            }

            trackToSync = local.copy(
                action = action
            )
        } else {
            trackToSync = TrackToSync(
                trackId = track.id,
                targets = listOf(
                    SyncTarget(
                        SyncApi.Shikimori,
                        track.shikimoriId
                    )
                ),
                action = action
            )
        }

        syncDao.insertOrUpdate(trackToSync)

        if (needSyncPendingTracks()) {
            trackTasks.syncPendingTracks()
        }
    }

    suspend fun needSyncPendingTracks(
        expiry: Instant = instantInPast(minutes = ExpiryConstants.SyncPendingTracks)
    ) = syncPendingTracksLastRequest.isRequestBefore(expiry)
}