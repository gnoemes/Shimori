package com.gnoemes.shimori.data.tracks

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.daos.TrackDao
import com.gnoemes.shimori.data.db.api.daos.TrackToSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class SyncedTrackStore(
    internal val dao: TrackDao,
    private val trackToSyncDao: TrackToSyncDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Track) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.sourceId, data)
            is Track -> sync(response.sourceId, data)
            is AnimeWithTrack -> sync(response.sourceId, data.track)
            is MangaWithTrack -> sync(response.sourceId, data.track)
            is RanobeWithTrack -> sync(response.sourceId, data.track)
            is AnimeInfo -> sync(response.sourceId, data.track)
//            is MangaInfo -> sync(response.sourceId, data.track)
//            is RanobeInfo -> sync(response.sourceId, data.track)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(sourceId: Long, data: List<E>) {
        when {
            data.filterIsInstance<Track>().isNotEmpty() -> sync(
                sourceId,
                null,
                data.filterIsInstance<Track>(),
            )

            data.filterIsInstance<AnimeWithTrack>().isNotEmpty() -> sync(
                sourceId,
                TrackTargetType.ANIME,
                data.filterIsInstance<AnimeWithTrack>().mapNotNull { it.track })

            data.filterIsInstance<MangaWithTrack>().isNotEmpty() -> sync(
                sourceId,
                TrackTargetType.MANGA,
                data.filterIsInstance<MangaWithTrack>().mapNotNull { it.track })

            data.filterIsInstance<RanobeWithTrack>().isNotEmpty() -> sync(
                sourceId,
                TrackTargetType.RANOBE,
                data.filterIsInstance<RanobeWithTrack>().mapNotNull { it.track })

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(sourceId: Long, remote: Track?) {
        if (remote == null) return

        val result = createSyncer(sourceId).sync(
            syncDao.findLocalId(sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(
        sourceId: Long,
        type: TrackTargetType?,
        remote: List<Track>
    ) {
        val result = createSyncer(sourceId).sync(
            currentValues = if (type == null) dao.queryAll() else dao.queryByTargetType(type),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer(
        sourceId: Long,
    ) = syncerForEntity(
        syncDao,
        type,
        sourceId,
        dao,
        entityToKey = ::findKey,
        networkEntityToKey = { _, track -> track.targetId to track.targetType },
        networkToId = { remote -> remote.id },
        mapper = { _, remote, local -> mapper(sourceId, remote, local) },
        logger
    )

    private fun findKey(sourceId: Long, local: Track): Pair<Long, TrackTargetType>? {
        val localTargetType = local.targetType
        val remoteTargetId = syncDao
            .findRemoteId(sourceId, local.targetId, localTargetType.sourceDataType)
            .let {
                //Check ranobe. On shikimori there's no difference between manga and ranobe in tracks
                if (it == null && localTargetType.ranobe) syncDao
                    .findRemoteId(sourceId, local.targetId, SourceDataType.Ranobe)
                else it
            }

        //we can't create track for unknown target
        if (remoteTargetId == null) {
            return null
        }

        //update track with same key
        return remoteTargetId to localTargetType
    }

    private fun mapper(sourceId: Long, remote: Track, local: Track?): Track {
        //if we have pending sync, we won't override local Track
        if (local != null) {
            val pendingSync = trackToSyncDao
                .queryByTrackId(local.id)
            if (pendingSync.isNotEmpty()) {
                return local
            }
        }

        return remote.copy(
            id = local?.id ?: 0,
            targetType = local?.targetType ?: remote.targetType,
            targetId = getTargetId(sourceId, remote, local)
        )
    }

    private fun getTargetId(sourceId: Long, remote: Track, local: Track?): Long {
        val targetId = local?.targetId?.takeIf { it > 0 }

        if (targetId != null) {
            return targetId
        }

        val remoteTargetType = remote.targetType

        return syncDao
            .findLocalId(sourceId, remote.targetId, remoteTargetType.sourceDataType) ?: 0
    }

    private fun log(result: ItemSyncerResult<Track>) {
        logger.i(tag = RESULT_TAG) {
            "Track sync results --> Added: ${result.added.size} Updated: ${result.updated.size} Deleted: ${result.deleted.size}"
        }
    }
}