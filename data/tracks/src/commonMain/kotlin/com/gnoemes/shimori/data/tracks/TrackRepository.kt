package com.gnoemes.shimori.data.tracks

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.app.SyncAction
import com.gnoemes.shimori.data.db.api.daos.TrackToSyncDao
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.GroupLastRequestStore
import com.gnoemes.shimori.data.source.track.TrackManager
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.track.TrackToSync
import com.gnoemes.shimori.data.user.UserShort
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

class TrackRepository(
    private val tracker: TrackManager,
    private val store: SyncedTrackStore,
    private val syncDao: TrackToSyncDao,
    private val groupLastRequestStore: GroupLastRequestStore,
    private val transactionRunner: DatabaseTransactionRunner,
) {
    fun queryById(id: Long) = store.dao.queryById(id)
    fun querySyncPendingTracks() = syncDao.queryAll()
    fun observeTracksExist() = store.dao.observeHasTracks()
    fun observeExistedStatuses(type: TrackTargetType) = store.dao.observeExistedStatuses(type)

    fun upsert(track: Track) = transactionRunner {
        val id = store.dao.upsert(
            track.copy(
                dateUpdated = Clock.System.now(),
                dateCreated = track.dateCreated ?: Clock.System.now()
            )
        )

        val local = store.dao.queryById(id)
        if (local != null) {
            val action =
                if (track.dateCreated == null) SyncAction.CREATE
                else SyncAction.UPDATE

            updateSyncPending(local, action)
        }
    }

    fun upsert(entity: TrackToSync) = syncDao.upsert(entity)

    fun delete(id: Long) = transactionRunner {
        val local = store.dao.queryById(id)

        local?.let {
            store.dao.delete(it)
            updateSyncPending(it, SyncAction.DELETE)
        }
    }

    fun delete(entity: TrackToSync) = syncDao.delete(entity)
    fun deleteByTrackId(entity: TrackToSync) = syncDao.deleteByTrackId(entity.trackId)

    suspend fun sync(user: UserShort): SourceResponse<List<Track>> {
        return tracker.track(user.sourceId, user) { getList(it) }
            .also {
                transactionRunner {
                    store.trySync(it)
                }
            }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
        }
    }

    fun pendingTracksSynced() = groupLastRequestStore.updateLastRequest(Request.SYNC_PENDING_TRACKS)

    fun needSyncPendingTracks(
        expiry: Instant = ExpiryConstants.SYNC_PENDING_TASKS.minutes.inPast
    ) = groupLastRequestStore.isRequestBefore(Request.SYNC_PENDING_TRACKS, expiry)

    private fun updateSyncPending(track: Track, action: SyncAction) {
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

        syncDao.upsert(trackToSync)

        if (needSyncPendingTracks()) {
            //TODO restore
//            trackTasks.syncPendingTracks()
        }
    }
}