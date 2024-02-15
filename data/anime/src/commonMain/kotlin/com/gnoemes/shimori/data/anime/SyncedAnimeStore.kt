package com.gnoemes.shimori.data.anime

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.AnimeDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject

@Inject
class SyncedAnimeStore(
    internal val dao: AnimeDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Anime) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.sourceId, data)
            is Anime -> sync(response.sourceId, data)
            is AnimeInfo -> sync(response.sourceId, data.entity)
            is AnimeWithTrack -> sync(response.sourceId, data.entity)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(sourceId: Long, data: List<E>) {
        when {
            data.filterIsInstance<Anime>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<Anime>()
            )

            data.filterIsInstance<AnimeWithTrack>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<AnimeWithTrack>().map { it.entity })

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(sourceId: Long, remote: Anime) {
        val result = createSyncer(sourceId).sync(
            syncDao.findLocalId(sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(sourceId: Long, remote: List<Anime>) {
        val result = createSyncer(sourceId).sync(
            currentValues = dao.queryAll(),
            networkValues = remote,
            removeNotMatched = false
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
        { _, title -> title.id },
        { _, title -> syncDao.findLocalId(sourceId, title.id, type) },
        { _, remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<Anime>) {
        logger.i(tag = RESULT_TAG) {
            "Anime sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}