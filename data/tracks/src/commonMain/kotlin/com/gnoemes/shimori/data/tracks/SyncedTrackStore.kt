package com.gnoemes.shimori.data.tracks

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.daos.TrackDao
import com.gnoemes.shimori.data.db.api.daos.TrackToSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.MangaOrRanobeWithTrack
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
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
            is List<*> -> trySync(response.params, data)
            is Track -> sync(response.params, data)
            is AnimeWithTrack -> sync(response.params, data.track)
            is MangaWithTrack -> sync(response.params, data.track)
            is RanobeWithTrack -> sync(response.params, data.track)
            is MangaOrRanobeWithTrack -> sync(response.params, data.track)
            is AnimeInfo -> sync(response.params, data.track)
            is MangaInfo -> sync(response.params, data.track)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<Track>().isNotEmpty() -> sync(
                params,
                null,
                data.filterIsInstance<Track>(),
            )

            data.filterIsInstance<AnimeWithTrack>().isNotEmpty() -> sync(
                params,
                TrackTargetType.ANIME,
                data.filterIsInstance<AnimeWithTrack>().mapNotNull { it.track })

            data.filterIsInstance<MangaWithTrack>().isNotEmpty() -> sync(
                params,
                TrackTargetType.MANGA,
                data.filterIsInstance<MangaWithTrack>().mapNotNull { it.track })

            data.filterIsInstance<RanobeWithTrack>().isNotEmpty() -> sync(
                params,
                TrackTargetType.RANOBE,
                data.filterIsInstance<RanobeWithTrack>().mapNotNull { it.track })

            data.filterIsInstance<AnimeInfo>().isNotEmpty() -> sync(
                params,
                TrackTargetType.ANIME,
                data.filterIsInstance<AnimeInfo>().mapNotNull { it.track })

            data.filterIsInstance<MangaInfo>().any { it.entity is Manga } -> sync(
                params,
                TrackTargetType.MANGA,
                data.filterIsInstance<MangaInfo>().mapNotNull { it.track })

            data.filterIsInstance<MangaInfo>().any { it.entity is Ranobe } -> sync(
                params,
                TrackTargetType.RANOBE,
                data.filterIsInstance<MangaInfo>().mapNotNull { it.track })

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(params: SourceParams, remote: Track?) {
        if (remote == null) return

        val result = createSyncer(params).sync(
            syncDao.findLocalId(params.sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(
        params: SourceParams,
        type: TrackTargetType?,
        remote: List<Track>
    ) {
        val result = createSyncer(params).sync(
            currentValues = if (type == null) dao.queryAll() else dao.queryByTargetType(type),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer(
        params: SourceParams
    ) = syncerForEntity(
        syncDao,
        type,
        params,
        dao,
        entityToKey = ::findKey,
        networkEntityToKey = { _, track -> track.targetId to track.targetType },
        networkToId = { remote -> remote.id },
        mapper = { _, remote, local -> mapper(params.sourceId, remote, local) },
        logger
    )

    private fun findKey(sourceId: Long, local: Track): Pair<Long, TrackTargetType>? {
        val localTargetType = local.targetType
        val remoteTargetId = syncDao.findRemoteId(sourceId, local.targetId, localTargetType.sourceDataType)
        //we can't create track for unknown target
            ?: return null


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