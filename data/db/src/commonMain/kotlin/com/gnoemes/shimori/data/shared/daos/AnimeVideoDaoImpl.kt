package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.AnimeVideoDao
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.ItemSyncerResult
import com.gnoemes.shimori.data.shared.SYNCER_RESULT_TAG
import com.gnoemes.shimori.data.shared.syncerForEntity
import com.gnoemes.shimori.data.shared.video
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

internal class AnimeVideoDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : AnimeVideoDao() {

    private val syncer = syncerForEntity(
        this,
        { it.id },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    override suspend fun insert(entity: AnimeVideo) {
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
    }

    override suspend fun update(entity: AnimeVideo) {
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

    override suspend fun delete(entity: AnimeVideo) {
        db.animeVideoQueries.deleteById(entity.id)
    }

    override fun observeByTitleId(id: Long) = db.animeVideoQueries
        .queryByTitleId(id, ::video)
        .asFlow()
        .mapToList(dispatchers.io)
        .flowOn(dispatchers.io)

    override suspend fun sync(titleId: Long, remote: List<AnimeVideo>) {
        val result: ItemSyncerResult<AnimeVideo>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.animeVideoQueries.queryByTitleId(titleId, ::video)
                    .executeAsList(),
                networkValues = remote.map { it.copy(titleId = titleId) },
                removeNotMatched = false
            )
        }

        logger.i(
            "Anime video sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }
}