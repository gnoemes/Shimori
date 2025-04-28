package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.db.api.daos.StudioDao
import com.gnoemes.shimori.data.util.studio
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = StudioDao::class)
class StudioDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : StudioDao, SqlDelightEntityDao<Studio> {

    override fun insert(entity: Studio): Long {
        entity.let {
            db.studioQueries.insert(
                it.sourceId,
                it.name,
                it.imageUrl
            )
        }
        return db.studioQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Studio) {
        db.studioQueries.update(
            id = entity.id,
            source_id = entity.sourceId,
            name = entity.name,
            image_url = entity.imageUrl,
        )
    }

    override fun delete(entity: Studio) {
        db.studioQueries.deleteById(entity.id)
    }

    override fun queryBySource(sourceId: Long): List<Studio> {
        return db.studioQueries.queryBySource(sourceId, ::studio)
            .executeAsList()
    }

    override fun countBySource(sourceId: Long): Long {
        return db.studioQueries.countBySource(sourceId)
            .executeAsOne()
    }

    override fun queryById(id: Long): Studio? {
        return db.studioQueries.queryById(id, ::studio)
            .executeAsOneOrNull()
    }

    override fun queryByTitle(targetId: Long, sourceId: Long): Studio? {
        return db.studioQueries.queryByTitle(sourceId, targetId, ::studio)
            .executeAsOneOrNull()
    }

    override fun observeByTitle(targetId: Long, sourceId: Long): Flow<Studio?> {
        return db.studioQueries.queryByTitle(sourceId, targetId, ::studio)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }
}