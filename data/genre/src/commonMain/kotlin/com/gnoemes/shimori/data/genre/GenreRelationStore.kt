package com.gnoemes.shimori.data.genre

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.common.GenreRelation
import com.gnoemes.shimori.data.db.api.daos.GenreRelationDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.EntityStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class GenreRelationStore(
    private val dao: GenreRelationDao,
    private val syncDao: SourceIdsSyncDao,
    logger: Logger
) : EntityStore(logger) {
    override val type: String = "Genre Relation"

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is AnimeInfo -> if (!data.genres.isNullOrEmpty()) sync(
                response.params,
                data.entity.id,
                data.entity.type,
                data.genres!!
            )

            is MangaInfo -> if (!data.genres.isNullOrEmpty()) sync(
                response.params,
                data.entity.id,
                data.entity.type,
                data.genres!!
            )

            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(
        params: SourceParams,
        remoteTitleId: Long,
        type: TrackTargetType,
        genres: List<Genre>
    ) {

        val titleId = syncDao.findLocalId(params.sourceId, remoteTitleId, type.sourceDataType)
            ?: return


        val relations = listOf(
            GenreRelation(
                id = 0,
                sourceId = params.sourceId,
                targetId = titleId,
                type = type,
                ids = genres.mapNotNull {
                    syncDao.findLocalId(params.sourceId, it.id, SourceDataType.Genre)
                }
            )
        )

        syncGenreRelations(params.sourceId, titleId, type, relations)
    }


    private fun syncGenreRelations(
        sourceId: Long,
        titleId: Long,
        type: TrackTargetType,
        relations: List<GenreRelation>,
    ) {
        val result = createSyncer().sync(
            currentValues = dao.queryRelationsByTitle(titleId, type, sourceId),
            networkValues = relations,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer() = syncerForEntity(
        dao,
        entityToKey = { relation: GenreRelation ->
            relation.targetId to relation.type
        },
        networkEntityToKey = { relation: GenreRelation ->
            relation.targetId to relation.type
        },
        mapper = { remote: GenreRelation, local: GenreRelation? ->
            remote.copy(id = local?.id ?: 0)
        },
        logger = logger
    )

    private fun log(result: ItemSyncerResult<GenreRelation>) {
        logger.i(tag = ItemSyncer.RESULT_TAG) {
            "$type sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }

}