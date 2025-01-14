package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.db.api.daos.AnimeScreenshotDao
import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.util.screenshot
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = AnimeScreenshotDao::class)
class AnimeScreenshotDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : AnimeScreenshotDao, SqlDelightEntityDao<AnimeScreenshot> {

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

    override fun queryByTitleId(id: Long): List<AnimeScreenshot> {
        return db.animeScreenshotQueries.queryByTitleId(id, ::screenshot).executeAsList()
    }

    override fun observeByTitleId(id: Long) = db.animeScreenshotQueries
        .queryByTitleId(id, ::screenshot)
        .asFlow()
        .mapToList(dispatchers.io)
        .flowOn(dispatchers.io)
}