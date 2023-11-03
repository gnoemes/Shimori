package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.db.api.daos.AnimeVideoDao
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.util.SYNCER_RESULT_TAG
import com.gnoemes.shimori.data.util.syncerForEntity
import com.gnoemes.shimori.data.util.video
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeVideoDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : AnimeVideoDao, SqlDelightEntityDao<AnimeVideo> {

    private val syncer = syncerForEntity(
        this,
        { it.url },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

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

    override fun observeByTitleId(id: Long) = db.animeVideoQueries
        .queryByTitleId(id, ::video)
        .asFlow()
        .mapToList(dispatchers.io)
        .flowOn(dispatchers.io)

    override suspend fun sync(titleId: Long, remote: List<AnimeVideo>) {
        val result = syncer.sync(
            currentValues = db.animeVideoQueries.queryByTitleId(titleId, ::video)
                .executeAsList(),
            networkValues = remote.map { it.copy(titleId = titleId) },
            removeNotMatched = true
        )

        logger.i(tag = SYNCER_RESULT_TAG) {
            "Anime video sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}