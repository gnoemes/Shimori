package com.gnoemes.shimori.data.manga

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.MangaDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject

@Inject
@ApplicationScope
class SyncedMangaStore(
    internal val dao: MangaDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Manga) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.sourceId, data)
//            is MangaInfo -> sync(response.sourceId, data.entity)
            is MangaWithTrack -> sync(response.sourceId, data.entity)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(sourceId: Long, data: List<E>) {
        when {
            data.filterIsInstance<Manga>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<Manga>()
            )

            data.filterIsInstance<MangaWithTrack>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<MangaWithTrack>().map { it.entity })

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(sourceId: Long, remote: Manga) {
        val result = createSyncer(sourceId).sync(
            syncDao.findLocalId(sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(sourceId: Long, remote: List<Manga>) {
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

    private fun log(result: ItemSyncerResult<Manga>) {
        logger.i(tag = RESULT_TAG) {
            "Manga sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}