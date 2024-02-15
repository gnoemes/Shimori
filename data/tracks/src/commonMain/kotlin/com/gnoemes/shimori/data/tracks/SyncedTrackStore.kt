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

@Inject
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
                data.filterIsInstance<Track>()
            )

            data.filterIsInstance<AnimeWithTrack>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<AnimeWithTrack>().mapNotNull { it.track })

            data.filterIsInstance<MangaWithTrack>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<MangaWithTrack>().mapNotNull { it.track })

            data.filterIsInstance<RanobeWithTrack>().isNotEmpty() -> sync(
                sourceId,
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

    private fun sync(sourceId: Long, remote: List<Track>) {
        val result = createSyncer(sourceId).sync(
            currentValues = dao.queryAll(),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer(
        sourceId: Long
    ) = syncerForEntity(
        syncDao,
        type,
        sourceId,
        dao,
        { _, track -> track.targetId to track.targetType },
        ::findKey,
        ::mapper,
        logger
    )

    private fun findKey(sourceId: Long, remote: Track): Pair<Long, TrackTargetType>? {
        val remoteTargetType = remote.targetType
        val (localTargetId, isRanobe) = syncDao
            .findLocalId(sourceId, remote.targetId, remoteTargetType.sourceDataType)
            .let { it to false }
            .let {
                //Check ranobe. On shikimori there's no difference between manga and ranobe in tracks
                if (it.first == null && remoteTargetType.manga) syncDao
                    .findLocalId(sourceId, remote.targetId, SourceDataType.Ranobe) to true
                else it
            }

        //we can't create track for unknown target
        if (localTargetId == null) {
            return null
        }

        //update track with same key
        return localTargetId to when (remoteTargetType) {
            TrackTargetType.ANIME -> remoteTargetType
            TrackTargetType.MANGA -> if (isRanobe) TrackTargetType.RANOBE else remoteTargetType
            else -> remoteTargetType
        }
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
            //do not override ranobe type
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
            .findLocalId(sourceId, remote.targetId, remoteTargetType.sourceDataType)
            .let {
                //Check ranobe. On shikimori there's no difference between manga and ranobe in tracks
                if (it == null && remoteTargetType.manga) syncDao
                    .findLocalId(sourceId, remote.targetId, SourceDataType.Ranobe)
                else it
            } ?: 0
    }

    private fun log(result: ItemSyncerResult<Track>) {
        logger.i(tag = RESULT_TAG) {
            "Track sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}