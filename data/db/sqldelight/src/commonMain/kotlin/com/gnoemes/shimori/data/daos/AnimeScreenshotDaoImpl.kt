package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.db.api.daos.AnimeScreenshotDao
import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.util.SYNCER_RESULT_TAG
import com.gnoemes.shimori.data.util.screenshot
import com.gnoemes.shimori.data.util.syncerForEntity
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeScreenshotDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : AnimeScreenshotDao, SqlDelightEntityDao<AnimeScreenshot> {

    private val syncer = syncerForEntity(
        this,
        { it.image.original },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    override fun insert(entity: AnimeScreenshot): Long {
        entity.let {
            db.animeScreenshotQueries.insert(
                it.titleId,
                it.image.original.orEmpty(),
                it.image.preview.orEmpty()
            )
        }
        return db.animeScreenshotQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: AnimeScreenshot) {
        entity.let {
            db.animeScreenshotQueries.update(
                it.id,
                it.titleId,
                it.image.original.orEmpty(),
                it.image.preview.orEmpty()
            )
        }
    }

    override fun delete(entity: AnimeScreenshot) {
        db.animeScreenshotQueries.deleteById(entity.id)
    }

    override fun observeByTitleId(id: Long) = db.animeScreenshotQueries
        .queryByTitleId(id, ::screenshot)
        .asFlow()
        .mapToList(dispatchers.io)
        .flowOn(dispatchers.io)

    override suspend fun sync(titleId: Long, remote: List<AnimeScreenshot>) {
        val result = syncer.sync(
            currentValues = db.animeScreenshotQueries.queryByTitleId(titleId, ::screenshot)
                .executeAsList(),
            networkValues = remote.map { it.copy(titleId = titleId) },
            removeNotMatched = true
        )

        logger.i(tag = SYNCER_RESULT_TAG) {
            "Anime screenshot sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}