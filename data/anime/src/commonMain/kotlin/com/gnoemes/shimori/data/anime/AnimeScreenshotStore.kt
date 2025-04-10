package com.gnoemes.shimori.data.anime

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.AnimeScreenshotDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.EntityStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class AnimeScreenshotStore(
    internal val dao: AnimeScreenshotDao,
    private val syncDao: SourceIdsSyncDao,
    logger: Logger
) : EntityStore(logger) {
    override val type: String = "Anime screenshot"

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is AnimeInfo -> sync(response.params, data.screenshots)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<AnimeScreenshot>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<AnimeScreenshot>()
            )

            data.filterIsInstance<AnimeInfo>().mapNotNull { it.screenshots }.isNotEmpty() -> sync(
                params,
                data.filterIsInstance<AnimeInfo>().mapNotNull { it.screenshots }.flatten()
            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(params: SourceParams, remote: List<AnimeScreenshot>?) {
        if (remote.isNullOrEmpty()) return

        val titleId = remote.firstOrNull()
            ?.let { syncDao.findLocalId(params.sourceId, it.titleId, SourceDataType.Anime) }
            ?: return

        syncScreenshots(titleId, remote.map { it.copy(titleId = titleId) })
    }

    private fun syncScreenshots(titleId: Long, remote: List<AnimeScreenshot>) {
        val result = createSyncer().sync(
            currentValues = dao.queryByTitleId(titleId),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer() = syncerForEntity(
        dao,
        { entity -> entity.image.original },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<AnimeScreenshot>) {
        logger.i(tag = ItemSyncer.RESULT_TAG) {
            "$type sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }

}