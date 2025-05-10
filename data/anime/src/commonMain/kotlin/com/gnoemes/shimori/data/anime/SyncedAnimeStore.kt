package com.gnoemes.shimori.data.anime

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.RelatedInfo
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
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class SyncedAnimeStore(
    internal val dao: AnimeDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Anime) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is Anime -> sync(response.params, data)
            is AnimeInfo -> sync(response.params, data.entity)
            is AnimeWithTrack -> sync(response.params, data.entity)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<Anime>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<Anime>()
            )

            data.filterIsInstance<AnimeInfo>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<AnimeInfo>().map { it.entity }
            )

            data.filterIsInstance<AnimeWithTrack>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<AnimeWithTrack>().map { it.entity })

            data.filterIsInstance<RelatedInfo>().any { it.title is Anime } -> sync(
                params,
                data.filterIsInstance<RelatedInfo>().map { it.title }
                    .filterIsInstance<Anime>()
            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(params: SourceParams, remote: Anime) {
        val result = createSyncer(params).sync(
            syncDao.findLocalId(params.sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(params: SourceParams, remote: List<Anime>) {
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

    private fun log(result: ItemSyncerResult<Anime>) {
        logger.i(tag = RESULT_TAG) {
            "Anime sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}