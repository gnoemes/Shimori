package com.gnoemes.shimori.data.daos

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.common.Related
import com.gnoemes.shimori.data.common.RelatedRelation
import com.gnoemes.shimori.data.db.api.daos.RelatedDao
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.related
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = RelatedDao::class)
class RelatedDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : RelatedDao, SqlDelightEntityDao<RelatedRelation> {

    override fun insert(entity: RelatedRelation): Long {
        entity.let {
            db.relatedQueries.insert(
                it.targetId,
                it.targetType,
                it.type,
                it.relation,
                it.relatedId,
                it.relatedType
            )
        }
        return db.relatedQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: RelatedRelation) {
        db.relatedQueries.update(
            entity.id,
            entity.targetId,
            entity.targetType,
            entity.type,
            entity.relation,
            entity.relatedId,
            entity.relatedType
        )
    }

    override fun delete(entity: RelatedRelation) {
        db.relatedQueries.deleteById(entity.id)
    }

    override fun queryByTitle(targetId: Long, targetType: TrackTargetType): List<RelatedRelation> {
        return db.relatedQueries.queryByTitle(targetId, targetType, ::RelatedRelation)
            .executeAsList()
    }

    override fun paging(
        targetId: Long,
        targetType: TrackTargetType,
    ): PagingSource<Int, Related> {
        fun query(
            limit: Long,
            offset: Long
        ) =
            db.relatedQueries.queryRelated(
                targetId,
                targetType,
                limit,
                offset,
                ::related
            )

        return QueryPagingSource(
            countQuery = db.relatedQueries.queryCount(targetId, targetType),
            transacter = db.relatedQueries,
            context = dispatchers.io,
            queryProvider = ::query
        )
    }
}