package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.db.api.daos.AnimeVideoDao
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.util.video
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = AnimeVideoDao::class)
class AnimeVideoDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : AnimeVideoDao, SqlDelightEntityDao<AnimeVideo> {

    override fun insert(entity: AnimeVideo): Long {
        entity.let {
            db.animeVideoQueries.insert(
                it.titleId,
                it.name,
                it.url,
                it.imageUrl,
                it.type.type,
                it.hosting
            )
        }
        return db.animeVideoQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: AnimeVideo) {
        entity.let {
            db.animeVideoQueries.update(
                it.id,
                it.titleId,
                it.name,
                it.url,
                it.imageUrl,
                it.type.type,
                it.hosting
            )
        }
    }

    override fun delete(entity: AnimeVideo) {
        db.animeVideoQueries.deleteById(entity.id)
    }

    override fun queryByTitleId(id: Long): List<AnimeVideo> {
        return db.animeVideoQueries.queryByTitleId(id, ::video).executeAsList()
    }

    override fun observeByTitleId(id: Long) = db.animeVideoQueries
        .queryByTitleId(id, ::video)
        .asFlow()
        .mapToList(dispatchers.io)
        .flowOn(dispatchers.io)
}