package com.gnoemes.shimori.data.studio

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.daos.StudioDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class SyncedStudioStore(
    internal val dao: StudioDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Studio) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is Studio -> sync(response.params, data)
            is AnimeInfo -> if (data.studio != null) sync(response.params, data.studio!!)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<Studio>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<Studio>()
            )

            data.filterIsInstance<AnimeInfo>().mapNotNull { it.studio }.isNotEmpty() -> sync(
                params,
                data.filterIsInstance<AnimeInfo>().mapNotNull { it.studio },
                removeNotMatched = false,
            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(params: SourceParams, remote: Studio) {
        val result = createSyncer(params).sync(
            syncDao.findLocalId(params.sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(params: SourceParams, remote: List<Studio>, removeNotMatched : Boolean = true) {
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
        entityToKey = { _, studio -> syncDao.findRemoteId(params.sourceId, studio.id, type) },
        networkEntityToKey = { _, studio -> studio.id },
        networkToId = { remote -> remote.id },
        mapper = { _, remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<Studio>) {
        logger.i(tag = RESULT_TAG) {
            "Studio sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}