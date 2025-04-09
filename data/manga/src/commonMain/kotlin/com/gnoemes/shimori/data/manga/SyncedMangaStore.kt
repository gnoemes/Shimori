package com.gnoemes.shimori.data.manga

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.MangaDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.MangaOrRanobeWithTrack
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class SyncedMangaStore(
    internal val dao: MangaDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Manga) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is MangaInfo -> if (data.entity is Manga) {
                sync(response.params, data.entity as Manga)
            }

            is MangaWithTrack -> sync(response.params, data.entity)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<Manga>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<Manga>()
            )

            data.filterIsInstance<MangaWithTrack>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<MangaWithTrack>().map { it.entity })

            data.filterIsInstance<MangaOrRanobeWithTrack>().any { it.type.manga } -> sync(
                params,
                data.filterIsInstance<MangaOrRanobeWithTrack>().map { it.entity }
                    .filterIsInstance<Manga>()
            )

            data.filterIsInstance<MangaInfo>().any { it.entity is Manga } -> sync(
                params,
                data.filterIsInstance<MangaInfo>().map { it.entity }
                    .filterIsInstance<Manga>()
            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(params: SourceParams, remote: Manga) {
        val result = createSyncer(params).sync(
            syncDao.findLocalId(params.sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(params: SourceParams, remote: List<Manga>) {
        val result = createSyncer(params).sync(
            currentValues = dao.queryAll(),
            networkValues = remote,
            removeNotMatched = false
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
        entityToKey = { _, title -> syncDao.findRemoteId(params.sourceId, title.id, type) },
        networkEntityToKey = { _, title -> title.id },
        networkToId = { remote -> remote.id },
        mapper = { _, remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<Manga>) {
        logger.i(tag = RESULT_TAG) {
            "Manga sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}