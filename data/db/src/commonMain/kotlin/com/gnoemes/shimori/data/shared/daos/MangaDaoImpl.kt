package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.MangaDao
import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
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

internal class MangaDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : MangaDao() {

    private val syncer = syncerForEntity(
        this,
        { _, title -> title.name.takeIf { it.isNotEmpty() } },
        { _, remote, _ -> remote },
        logger
    )

    override suspend fun insert(sourceId: Long, remote: Manga) {
        db.withTransaction {
            remote.let {
                mangaQueries.insert(
                    it.name,
                    it.nameRu,
                    it.nameEn,
                    it.image?.original,
                    it.image?.preview,
                    it.image?.x96,
                    it.image?.x48,
                    it.url,
                    it.mangaType?.type,
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
                val localId = mangaQueries.selectLastInsertedRowId().executeAsOne()
                syncRemoteIds(sourceId, localId, remote.id, syncDataType)
            }
        }
    }

    override suspend fun update(sourceId: Long, remote: Manga, local: Manga) {
        db.withTransaction {
            remote.let {
                mangaQueries.update(
                    local.id,
                    it.name,
                    it.nameRu,
                    it.nameEn,
                    it.image?.original,
                    it.image?.preview,
                    it.image?.x96,
                    it.image?.x48,
                    it.url,
                    it.mangaType?.type,
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
                syncRemoteIds(sourceId, local.id, remote.id, syncDataType)
            }
        }
    }

    override suspend fun delete(sourceId: Long, local: Manga) {
        db.withTransaction {
            mangaQueries.deleteById(local.id)
            sourceIdsSyncQueries.deleteByLocal(local.id, syncDataType.type)
        }
    }

    override suspend fun queryById(id: Long): Manga? {
        return db.mangaQueries.queryById(id, ::manga)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Manga> {
        TODO("Not yet implemented")
    }

    override suspend fun sync(sourceId: Long, remote: List<Manga>) {
        val result: ItemSyncerResult<Manga>
        val time = measureTimeMillis {
            result = syncer.sync(
                sourceId = sourceId,
                currentValues = db.mangaQueries.queryAll(::manga).executeAsList(),
                networkValues = remote,
                removeNotMatched = false
            )
        }

        logger.i(
            "Manga sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override fun observeById(id: Long): Flow<MangaWithTrack?> {
        return db.mangaQueries.queryByIdWithTrack(id, ::mangaWithTrack)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override suspend fun queryByStatus(status: TrackStatus): List<MangaWithTrack> {
        return db.mangaQueries.queryByStatus(status, ::mangaWithTrack)
            .executeAsList()
    }


    override fun paging(status: TrackStatus, sort: ListSort): PagingSource<Long, PaginatedEntity> {
        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            ListSortOption.NAME -> db.mangaListViewQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
            ListSortOption.PROGRESS -> db.mangaListViewQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
            ListSortOption.DATE_CREATED -> db.mangaListViewQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
            ListSortOption.DATE_UPDATED -> db.mangaListViewQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
            ListSortOption.DATE_AIRED -> db.mangaListViewQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
            ListSortOption.MY_SCORE -> db.mangaListViewQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
            ListSortOption.SIZE -> db.mangaListViewQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
            ListSortOption.RATING -> db.mangaListViewQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
        }

        return QueryPaging(
            countQuery = db.mangaQueries.countWithStatus(status),
            transacter = db.mangaQueries,
            dispatcher = dispatchers.io,
            queryProvider = ::query
        )
    }
}