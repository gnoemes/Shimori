package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.adapters.GenreIdsAdapter
import com.gnoemes.shimori.data.common.GenreRelation
import com.gnoemes.shimori.data.db.api.daos.GenreRelationDao
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.genreRelation
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = GenreRelationDao::class)
class GenreRelationDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : GenreRelationDao(), SqlDelightEntityDao<GenreRelation> {

    override fun insert(entity: GenreRelation): Long {
        entity.let {
            db.genreRelationQueries.insert(
                it.targetId,
                it.type,
                it.sourceId,
                GenreIdsAdapter.encode(it.ids),
            )
        }
        return db.genreRelationQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: GenreRelation) {
        db.genreRelationQueries.update(
            entity.id,
            entity.targetId,
            entity.type,
            entity.sourceId,
            GenreIdsAdapter.encode(entity.ids),
        )
    }

    override fun delete(entity: GenreRelation) {
        db.genreRelationQueries.deleteById(entity.id)
    }

    override fun queryRelationsByTitle(
        id: Long,
        type: TrackTargetType,
        sourceId: Long
    ): List<GenreRelation> {
        return db.genreRelationQueries.queryByTitleAndSource(id, type, sourceId, ::genreRelation)
            .executeAsList()
    }

}