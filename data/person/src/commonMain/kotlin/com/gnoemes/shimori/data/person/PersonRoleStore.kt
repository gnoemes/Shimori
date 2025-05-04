package com.gnoemes.shimori.data.person

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.PersonRoleDao
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
class PersonRoleStore(
    private val personRoleDao: PersonRoleDao,
    private val dao: SourceIdsSyncDao,
    logger: Logger
) : EntityStore(logger) {
    override val type: String = "Person Role"

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is AnimeInfo -> data.personsRoles?.takeIf { it.isNotEmpty() }
                ?.let { sync(response.params, it) }

            is MangaInfo -> data.personsRoles?.takeIf { it.isNotEmpty() }
                ?.let { sync(response.params, it) }

//            is PersonInfo -> sync(response.params, data)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
//            data.filterIsInstance<CharacterRole>().isNotEmpty() -> sync(
//                params,
//                data.filterIsInstance<CharacterRole>(),
//                fromTitle = false
//            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    /**
     * Sync by title
     */
    private fun sync(
        params: SourceParams,
        remote: List<PersonRole>,
    ) {
        val firstRole = remote.first()
        val targetId = dao.findLocalId(
            params.sourceId,
            firstRole.targetId,
            firstRole.targetType.sourceDataType
        ) ?: return

        val result = createSyncer(params).sync(
            currentValues = personRoleDao.queryByTitle(targetId, firstRole.targetType),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer(params: SourceParams) = syncerForEntity(
        personRoleDao,
        entityToKey = { findKey(params, it) },
        networkEntityToKey = { role -> Triple(role.personId, role.targetId, role.targetType) },
        mapper = { remote, local -> mapper(params, remote, local) },
        logger = logger
    )

    private fun findKey(
        params: SourceParams,
        role: PersonRole
    ): Triple<Long, Long, TrackTargetType>? {
        val remotePersonId =
            dao.findRemoteId(params.sourceId, role.personId, SourceDataType.Person)
        val remoteTargetId =
            dao.findRemoteId(params.sourceId, role.targetId, role.targetType.sourceDataType)

        if (remotePersonId == null || remoteTargetId == null) return null

        return Triple(remotePersonId, remoteTargetId, role.targetType)
    }

    private fun mapper(
        params: SourceParams,
        remote: PersonRole,
        local: PersonRole?
    ): PersonRole {
        if (local != null) {
            return remote.copy(
                id = local.id,
                personId = local.personId,
                targetId = local.targetId
            )
        }

        val localPersonId =
            dao.findLocalId(params.sourceId, remote.personId, SourceDataType.Person)
        val localTargetId =
            dao.findLocalId(params.sourceId, remote.targetId, remote.targetType.sourceDataType)

        if (localPersonId == null || localTargetId == null) {
            return remote
        }

        return remote.copy(
            id = 0,
            personId = localPersonId,
            targetId = localTargetId
        )
    }

    private fun log(result: ItemSyncerResult<PersonRole>) {
        logger.i(tag = ItemSyncer.RESULT_TAG) {
            "$type sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}