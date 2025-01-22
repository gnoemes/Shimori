package com.gnoemes.shimori.data.ranobe

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.RanobeDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class SyncedRanobeStore(
    internal val dao: RanobeDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Ranobe) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.sourceId, data)
//            is RanobeInfo -> sync(response.sourceId, data.entity)
            is RanobeWithTrack -> sync(response.sourceId, data.entity)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(sourceId: Long, data: List<E>) {
        when {
            data.filterIsInstance<Ranobe>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<Ranobe>()
            )

            data.filterIsInstance<RanobeWithTrack>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<RanobeWithTrack>().map { it.entity })

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(sourceId: Long, remote: Ranobe) {
        val result = createSyncer(sourceId).sync(
            syncDao.findLocalId(sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(sourceId: Long, remote: List<Ranobe>) {
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
        entityToKey = { _, title -> syncDao.findRemoteId(sourceId, title.id, type) },
        networkEntityToKey = { _, title -> title.id },
        networkToId = { remote -> remote.id },
        mapper = { _, remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<Ranobe>) {
        logger.i(tag = RESULT_TAG) {
            "Ranobe sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}