package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.RanobeDao
import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
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
        { _, title -> title.name.takeIf { it.isNotEmpty() } },
        { _, remote, _ -> remote },
        logger
    )

    override suspend fun insert(sourceId: Long, remote: Ranobe) {
        db.withTransaction {
            remote.let {
                ranobeQueries.insert(
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
                val localId = ranobeQueries.selectLastInsertedRowId().executeAsOne()
                syncRemoteIds(sourceId, localId, it.id, syncDataType)
            }
        }
    }

    override suspend fun update(sourceId: Long, remote: Ranobe, local: Ranobe) {
        db.withTransaction {
            remote.let {
                ranobeQueries.update(
                    local.id,
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

                syncRemoteIds(sourceId, local.id, it.id, syncDataType)
            }
        }
    }

    override suspend fun delete(sourceId: Long, local: Ranobe) {
        db.withTransaction {
            ranobeQueries.deleteById(local.id)
            sourceIdsSyncQueries.deleteByLocal(local.id, syncDataType.type)
        }
    }

    override suspend fun sync(sourceId: Long, remote: List<Ranobe>) {
        val result: ItemSyncerResult<Ranobe>
        val time = measureTimeMillis {
            result = syncer.sync(
                sourceId = sourceId,
                currentValues = db.ranobeQueries.queryAll(::ranobe).executeAsList(),
                networkValues = remote,
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

    override suspend fun queryByStatus(status: TrackStatus): List<RanobeWithTrack> {
        return db.ranobeQueries.queryByStatus(status, ::ranobeWithTrack)
            .executeAsList()
    }

    override fun observeById(id: Long): Flow<RanobeWithTrack?> {
        return db.ranobeQueries.queryByIdWithTrack(id, ::ranobeWithTrack)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun paging(status: TrackStatus, sort: ListSort): PagingSource<Long, PaginatedEntity> {

        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            ListSortOption.NAME -> db.ranobeListViewQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
            ListSortOption.PROGRESS -> db.ranobeListViewQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
            ListSortOption.DATE_CREATED -> db.ranobeListViewQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
            ListSortOption.DATE_UPDATED -> db.ranobeListViewQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
            ListSortOption.DATE_AIRED -> db.ranobeListViewQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
            ListSortOption.MY_SCORE -> db.ranobeListViewQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
            ListSortOption.SIZE -> db.ranobeListViewQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
            ListSortOption.RATING -> db.ranobeListViewQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
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