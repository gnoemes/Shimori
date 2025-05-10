package com.gnoemes.shimori.data.core

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.RelatedInfo
import com.gnoemes.shimori.data.common.RelatedRelation
import com.gnoemes.shimori.data.db.api.daos.RelatedDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.EntityStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class RelatedStore(
    private val dao: RelatedDao,
    private val syncDao: SourceIdsSyncDao,
    logger: Logger
) : EntityStore(logger) {
    override val type: String = "Related"

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is AnimeInfo -> if (data.related != null) sync(
                response.params,
                data.entity.id,
                data.entity.type,
                data.related!!.map { it.relation }
            )

            is MangaInfo -> if (data.related != null) sync(
                response.params,
                data.entity.id,
                data.entity.type,
                data.related!!.map { it.relation }
            )

            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<RelatedInfo>().isNotEmpty() -> data
                .filterIsInstance<RelatedInfo>()
                .first().let {
                    sync(
                        params,
                        it.titleId,
                        it.titleType,
                        data.filterIsInstance<RelatedInfo>().map { it.relation }
                    )

                }

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(
        params: SourceParams,
        titleId: Long,
        titleType: TrackTargetType,
        relatedRelation: List<RelatedRelation>,
    ) {
        val localId = syncDao.findLocalId(params.sourceId, titleId, titleType.sourceDataType)
            ?: return

        val result = createSyncer(params).sync(
            currentValues = dao.queryByTitle(localId, titleType),
            networkValues = relatedRelation,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer(
        params: SourceParams,
    ) = syncerForEntity(
        dao,
        entityToKey = { findKey(params, it) },
        networkEntityToKey = {
            listOf(it.targetId, it.targetType, it.relatedId, it.relatedType).hashCode()
        },
        mapper = { remote, local -> mapper(params, remote, local) },
        logger = logger
    )

    private fun findKey(
        params: SourceParams,
        relatedRelation: RelatedRelation
    ): Int? {
        val remoteTargetId =
            syncDao.findRemoteId(
                params.sourceId,
                relatedRelation.targetId,
                relatedRelation.targetType.sourceDataType
            )
        val remoteRelatedId =
            syncDao.findRemoteId(
                params.sourceId,
                relatedRelation.relatedId,
                relatedRelation.relatedType.sourceDataType
            )

        if (remoteTargetId == null || remoteRelatedId == null) return null

        return listOf(
            remoteTargetId,
            relatedRelation.targetType,
            remoteRelatedId,
            relatedRelation.relatedType
        ).hashCode()
    }

    private fun mapper(
        params: SourceParams,
        remote: RelatedRelation,
        local: RelatedRelation?
    ): RelatedRelation {
        if (local != null) {
            return remote.copy(
                id = local.id,
                targetId = local.targetId,
                relatedId = local.relatedId
            )
        }

        val localTargetId =
            syncDao.findLocalId(params.sourceId, remote.targetId, remote.targetType.sourceDataType)
        val localRelatedId =
            syncDao.findLocalId(
                params.sourceId,
                remote.relatedId,
                remote.relatedType.sourceDataType
            )

        if (localTargetId == null || localRelatedId == null) {
            return remote
        }

        return remote.copy(
            id = 0,
            targetId = localTargetId,
            relatedId = localRelatedId
        )
    }

    private fun log(result: ItemSyncerResult<RelatedRelation>) {
        logger.i(tag = ItemSyncer.RESULT_TAG) {
            "$type sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }

}