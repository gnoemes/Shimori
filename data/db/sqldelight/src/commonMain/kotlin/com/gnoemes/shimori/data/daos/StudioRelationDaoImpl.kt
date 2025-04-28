package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.common.StudioRelation
import com.gnoemes.shimori.data.db.api.daos.StudioRelationDao
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = StudioRelationDao::class)
class StudioRelationDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : StudioRelationDao, SqlDelightEntityDao<StudioRelation> {

    override fun insert(entity: StudioRelation): Long {
        entity.let {
            db.studioRelationQueries.insert(
                it.studioId,
                it.sourceId,
                it.targetId,
            )
        }
        return db.studioRelationQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: StudioRelation) {
        db.studioRelationQueries.update(
            entity.id,
            entity.studioId,
            entity.sourceId,
            entity.targetId,
        )
    }

    override fun delete(entity: StudioRelation) {
        db.studioRelationQueries.deleteById(entity.id)
    }

    override fun queryByTitle(sourceId: Long, targetId: Long): List<StudioRelation> {
        return db.studioRelationQueries.queryRelationsByTitle(sourceId, targetId, ::StudioRelation)
            .executeAsList()
    }
}