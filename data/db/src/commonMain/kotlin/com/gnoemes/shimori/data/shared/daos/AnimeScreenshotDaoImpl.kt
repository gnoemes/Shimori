package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.AnimeScreenshotDao
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.ItemSyncerResult
import com.gnoemes.shimori.data.shared.SYNCER_RESULT_TAG
import com.gnoemes.shimori.data.shared.screenshot
import com.gnoemes.shimori.data.shared.syncerForEntity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

internal class AnimeScreenshotDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : AnimeScreenshotDao() {

    private val syncer = syncerForEntity(
        this,
        { it.image.original },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    override suspend fun insert(entity: AnimeScreenshot) {
        entity.let {
            db.animeScreenshotQueries.insert(
                it.titleId,
                it.image.original.orEmpty(),
                it.image.preview.orEmpty()
            )
        }
    }

    override suspend fun update(entity: AnimeScreenshot) {
        entity.let {
            db.animeScreenshotQueries.update(
                it.id,
                it.titleId,
                it.image.original.orEmpty(),
                it.image.preview.orEmpty()
            )
        }
    }

    override suspend fun delete(entity: AnimeScreenshot) {
        db.animeScreenshotQueries.deleteById(entity.id)
    }

    override fun observeByTitleId(id: Long) = db.animeScreenshotQueries
        .queryByTitleId(id, ::screenshot)
        .asFlow()
        .mapToList(dispatchers.io)
        .flowOn(dispatchers.io)

    override suspend fun sync(titleId: Long, remote: List<AnimeScreenshot>) {
        val result: ItemSyncerResult<AnimeScreenshot>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.animeScreenshotQueries.queryByTitleId(titleId, ::screenshot)
                    .executeAsList(),
                networkValues = remote.map { it.copy(titleId = titleId) },
                removeNotMatched = true
            )
        }

        logger.i(
            "Anime screenshot sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }
}