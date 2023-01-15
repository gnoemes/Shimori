package com.gnoemes.shimori.data.repositories.track

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.base.core.tasks.TrackTasks
import com.gnoemes.shimori.data.core.database.daos.ListSortDao
import com.gnoemes.shimori.data.core.database.daos.TrackDao
import com.gnoemes.shimori.data.core.database.daos.TrackToSyncDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.app.SyncAction
import com.gnoemes.shimori.data.core.entities.track.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class TrackRepository(
    private val dao: TrackDao,
    private val syncDao: TrackToSyncDao,
    private val listSortDao: ListSortDao,
    private val trackTasks: TrackTasks,
    private val syncPendingTracksLastRequest: SyncPendingTracksLastRequestStore
) {
    suspend fun queryById(id: Long) = dao.queryById(id)

    suspend fun querySyncPendingTracks() = syncDao.queryAll()

    fun observeTracksExist() = dao.observeHasTracks()
    fun observeListSort(type: ListType): Flow<ListSort?> = listSortDao.observe(type)
    fun observeExistedStatuses(type: TrackTargetType) = dao.observeExistedStatuses(type)

    suspend fun sync(
        sourceId: Long,
        targetType: TrackTargetType,
        trackStatus: TrackStatus?,
        remote: List<Track>
    ) = dao.sync(sourceId, targetType, trackStatus, remote)

    suspend fun sync(sourceId: Long, remote: List<Track>) = dao.sync(sourceId, remote)
    suspend fun sync(sourceId: Long, remote: Track) = dao.sync(sourceId, arrayListOf(remote))

    suspend fun createOrUpdate(track: Track) {
        val localId = dao.insertOrUpdate(
            track.copy(
                dateUpdated = Clock.System.now(),
                dateCreated = track.dateCreated ?: Clock.System.now()
            )
        )

        val local = dao.queryById(localId)
        if (local != null) {
            val action =
                if (track.dateCreated == null) SyncAction.CREATE
                else SyncAction.UPDATE

            updateSyncPending(local, action)
        }
    }

    suspend fun createOrUpdate(trackToSync: TrackToSync) = syncDao.insertOrUpdate(trackToSync)
    suspend fun createOrUpdate(listSort: ListSort) = listSortDao.insertOrUpdate(listSort)


    suspend fun delete(id: Long) {
        val local = dao.queryById(id)

        local?.let {
            dao.delete(it)
            updateSyncPending(it, SyncAction.DELETE)
        }
    }

    suspend fun delete(trackToSync: TrackToSync) = syncDao.delete(trackToSync)
    suspend fun deleteByTrackId(trackToSync: TrackToSync) =
        syncDao.deleteByTrackId(trackToSync.trackId)

    private suspend fun updateSyncPending(track: Track, action: SyncAction) {
        val list = syncDao.queryByTrackId(track.id)
        val local = list.find { it.attemptSourceId == null }

        //we can delete all non synced pending tracks
        list.filter { it.attemptSourceId != null }
            .forEach { trackToSync -> syncDao.delete(trackToSync) }

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