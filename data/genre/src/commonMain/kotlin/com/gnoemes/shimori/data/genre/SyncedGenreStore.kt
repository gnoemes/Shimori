package com.gnoemes.shimori.data.genre

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.db.api.daos.GenreDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class SyncedGenreStore(
    internal val dao: GenreDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Genre) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is Genre -> sync(response.params, data)
            is AnimeInfo -> if (!data.genres.isNullOrEmpty()) sync(
                response.params,
                data.genres!!,
                removeNotMatched = false,
            )

            is MangaInfo -> if (!data.genres.isNullOrEmpty()) sync(
                response.params,
                data.genres!!,
                removeNotMatched = false,
            )

            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<Genre>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<Genre>()
            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(params: SourceParams, remote: Genre) {
        val result = createSyncer(params).sync(
            syncDao.findLocalId(params.sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(params: SourceParams, remote: List<Genre>, removeNotMatched: Boolean = true) {
        val result = createSyncer(params).sync(
            currentValues = dao.queryBySource(params.sourceId),
            networkValues = remote,
            removeNotMatched = removeNotMatched
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
        entityToKey = { _, genre -> syncDao.findRemoteId(params.sourceId, genre.id, type) },
        networkEntityToKey = { _, genre -> genre.id },
        networkToId = { remote -> remote.id },
        mapper = { _, remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<Genre>) {
        logger.i(tag = RESULT_TAG) {
            "Genre sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}