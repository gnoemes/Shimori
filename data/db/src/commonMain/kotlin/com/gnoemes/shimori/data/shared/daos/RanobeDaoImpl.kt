package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.RanobeDao
import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.paging.PagingSource
import com.gnoemes.shimori.data.shared.*
import com.gnoemes.shimori.data.shared.paging.QueryPaging
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

internal class RanobeDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : RanobeDao() {

    private val syncer = syncerForEntity(
        this,
        { it.shikimoriId },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    override suspend fun insert(entity: Ranobe) {
        entity.let {
            db.ranobeQueries.insert(
                it.shikimoriId,
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.ranobeType?.type,
                it.rating,
                it.status,
                it.chapters,
                it.volumes,
                it.dateAired,
                it.dateReleased,
                it.ageRating,
                it.description,
                it.descriptionHtml,
                it.franchise,
                it.favorite,
                it.topicId,
                it.genres
            )
        }
    }

    override suspend fun update(entity: Ranobe) {
        entity.let {
            db.ranobeQueries.update(
                it.id,
                it.shikimoriId,
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.ranobeType?.type,
                it.rating,
                it.status?.let(TitleStatusAdapter::encode),
                it.chapters,
                it.volumes,
                it.dateAired?.let(LocalDateAdapter::encode),
                it.dateReleased?.let(LocalDateAdapter::encode),
                it.ageRating.let(AgeRatingAdapter::encode),
                it.description,
                it.descriptionHtml,
                it.franchise,
                it.favorite,
                it.topicId,
                it.genres?.let(GenresAdapter::encode),
            )
        }
    }

    override suspend fun delete(entity: Ranobe) {
        db.ranobeQueries.deleteById(entity.id)
    }

    override suspend fun insertOrUpdate(entities: List<Ranobe>) {
        val result: ItemSyncerResult<Ranobe>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.ranobeQueries.queryAll(::ranobe).executeAsList(),
                networkValues = entities,
                removeNotMatched = false
            )
        }

        logger.i(
            "Ranobe sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun queryById(id: Long): Ranobe? {
        return db.ranobeQueries.queryById(id, ::ranobe)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Ranobe> {
        TODO("Not yet implemented")
    }

    override suspend fun queryByStatus(status: RateStatus): List<RanobeWithRate> {
        return db.ranobeQueries.queryByStatus(status, ::ranobeWithRate)
            .executeAsList()
    }

    override fun observeById(id: Long): Flow<RanobeWithRate?> {
        return db.ranobeQueries.queryByIdWithRate(id, ::ranobeWithRate)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun paging(status: RateStatus, sort: RateSort): PagingSource<Long, PaginatedEntity> {

        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            RateSortOption.NAME -> db.ranobeQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
            RateSortOption.PROGRESS -> db.ranobeQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
            RateSortOption.DATE_CREATED -> db.ranobeQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
            RateSortOption.DATE_UPDATED -> db.ranobeQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
            RateSortOption.DATE_AIRED -> db.ranobeQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
            RateSortOption.MY_SCORE -> db.ranobeQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
            RateSortOption.SIZE -> db.ranobeQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
            RateSortOption.RATING -> db.ranobeQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::ranobeWithRate
            )
        }

        return QueryPaging(
            countQuery = db.ranobeQueries.countWithStatus(status),
            transacter = db.ranobeQueries,
            dispatcher = dispatchers.io,
            queryProvider = ::query
        )
    }
}