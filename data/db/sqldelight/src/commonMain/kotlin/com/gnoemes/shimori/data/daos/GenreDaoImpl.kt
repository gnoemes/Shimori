package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.db.api.daos.GenreDao
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.genre
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = GenreDao::class)
class GenreDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : GenreDao(), SqlDelightEntityDao<Genre> {

    override fun insert(entity: Genre): Long {
        entity.let {
            db.genreQueries.insert(
                it.sourceId,
                it.type,
                it.name,
                it.nameRu,
                it.description
            )
        }
        return db.genreQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Genre) {
        db.genreQueries.update(
            id = entity.id,
            source_id = entity.sourceId,
            type = entity.type,
            name = entity.name,
            name_ru = entity.nameRu,
            description = entity.description
        )
    }

    override fun delete(entity: Genre) {
        db.genreQueries.deleteById(entity.id)
    }

    override fun queryAll(): List<Genre> {
        return db.genreQueries.queryAll(::genre)
            .executeAsList()
    }

    override fun queryBySource(sourceId: Long): List<Genre> {
        return db.genreQueries.queryBySource(sourceId, ::genre)
            .executeAsList()
    }

    override fun countBySource(sourceId: Long): Long {
        return db.genreQueries.countBySource(sourceId)
            .executeAsOne()
    }

    override fun queryByTitle(id: Long, type: TrackTargetType, sourceId: Long): List<Genre> {
        return db.genreQueries.queryByTitle(id, type, sourceId, ::genre)
            .executeAsList()
    }

    override fun queryById(id: Long): Genre? {
        return db.genreQueries.queryById(id, ::genre)
            .executeAsOneOrNull()
    }

    override fun observeByTitle(
        id: Long,
        type: TrackTargetType,
        sourceId: Long
    ): Flow<List<Genre>> {
        return db.genreQueries.queryByTitle(id, type, sourceId, ::genre)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }
}