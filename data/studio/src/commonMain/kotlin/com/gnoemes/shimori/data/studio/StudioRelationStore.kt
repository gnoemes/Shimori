package com.gnoemes.shimori.data.studio

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.common.StudioRelation
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.daos.StudioRelationDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.EntityStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class StudioRelationStore(
    private val dao: StudioRelationDao,
    private val syncDao: SourceIdsSyncDao,
    logger: Logger
) : EntityStore(logger) {
    override val type: String = "Studio Relation"

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is AnimeInfo -> if (data.studio != null) sync(
                response.params,
                data.entity.id,
                data.studio!!
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
        studio: Studio
    ) {

        val titleId = syncDao.findLocalId(params.sourceId, remoteTitleId, SourceDataType.Anime)
        if (titleId == null) {
            logger.i(tag = type) { "Anime with remote id $remoteTitleId not found" }
            return
        }

        val studioId = syncDao.findLocalId(params.sourceId, remoteTitleId, SourceDataType.Studio)
        if (studioId == null) {
            logger.i(tag = type) { "Studio ${studio.name} with remote id ${studio.id} not found" }
            return
        }

        val relations = listOf(
            StudioRelation(
                id = 0,
                studioId = studioId,
                sourceId = params.sourceId,
                targetId = titleId,
            )
        )

        syncStudioRelations(params.sourceId, titleId, relations)
    }


    private fun syncStudioRelations(
        sourceId: Long,
        titleId: Long,
        relations: List<StudioRelation>,
    ) {
        val result = createSyncer().sync(
            currentValues = dao.queryByTitle(sourceId, titleId)
                ?.let { listOf(it) } ?: emptyList(),
            networkValues = relations,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer() = syncerForEntity(
        dao,
        entityToKey = { relation: StudioRelation ->
            relation.sourceId to relation.targetId
        },
        networkEntityToKey = { relation: StudioRelation ->
            relation.sourceId to relation.targetId
        },
        mapper = { remote: StudioRelation, local: StudioRelation? ->
            remote.copy(id = local?.id ?: 0)
        },
        logger = logger
    )

    private fun log(result: ItemSyncerResult<StudioRelation>) {
        logger.i(tag = ItemSyncer.RESULT_TAG) {
            "$type sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }

}