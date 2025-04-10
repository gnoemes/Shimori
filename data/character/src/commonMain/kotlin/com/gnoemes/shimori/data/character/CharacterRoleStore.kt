package com.gnoemes.shimori.data.character

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.db.api.daos.CharacterRoleDao
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
class CharacterRoleStore(
    private val characterRoleDao: CharacterRoleDao,
    private val dao: SourceIdsSyncDao,
    logger: Logger
) : EntityStore(logger) {
    override val type: String = "Character Role"

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is AnimeInfo -> data.charactersRoles?.takeIf { it.isNotEmpty() }
                ?.let { sync(response.params, it) }

            is MangaInfo -> data.charactersRoles?.takeIf { it.isNotEmpty() }
                ?.let { sync(response.params, it) }

            is CharacterInfo -> sync(response.params, data)
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

    private fun sync(params: SourceParams, remote: CharacterInfo) {
        val characterId = dao.findLocalId(
            params.sourceId,
            remote.entity.id,
            SourceDataType.Character
        ) ?: return

        val roles = mutableListOf<CharacterRole>()

        if (remote.animes != null) {
            for (title in remote.animes!!) {
                val id =
                    dao.findLocalId(params.sourceId, title.id, SourceDataType.Anime) ?: continue

                roles += CharacterRole(
                    characterId = characterId,
                    targetId = id,
                    targetType = TrackTargetType.ANIME
                )
            }
        }
        if (remote.mangas != null) {
            for (title in remote.mangas!!) {
                val (id, type) = dao.findLocalId(
                    params.sourceId,
                    title.id,
                    SourceDataType.Manga
                )?.let { it to TrackTargetType.MANGA } ?: dao.findLocalId(
                    params.sourceId,
                    title.id,
                    SourceDataType.Ranobe
                )?.let { it to TrackTargetType.RANOBE } ?: continue

                roles += CharacterRole(
                    characterId = characterId,
                    targetId = id,
                    targetType = type
                )
            }
        }

        if (roles.isNotEmpty()) {
            sync(params, characterId, roles)
        }
    }


    /**
     * Sync by character details
     */
    private fun sync(
        params: SourceParams,
        characterId: Long,
        remote: List<CharacterRole>
    ) {
        val result = createSyncer(params).sync(
            currentValues = characterRoleDao.queryByCharacterId(characterId),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    /**
     * Sync by title
     */
    private fun sync(
        params: SourceParams,
        remote: List<CharacterRole>,
    ) {
        val firstRole = remote.first()
        val targetId = dao.findLocalId(
            params.sourceId,
            firstRole.targetId,
            firstRole.targetType.sourceDataType
        ) ?: return

        val result = createSyncer(params).sync(
            currentValues = characterRoleDao.queryByTitle(targetId, firstRole.targetType),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    private fun createSyncer(params: SourceParams) = syncerForEntity(
        characterRoleDao,
        entityToKey = { findKey(params, it) },
        networkEntityToKey = { role -> Triple(role.characterId, role.targetId, role.targetType) },
        mapper = { remote, local -> mapper(params, remote, local) },
        logger = logger
    )

    private fun findKey(
        params: SourceParams,
        role: CharacterRole
    ): Triple<Long, Long, TrackTargetType>? {
        val remoteCharacterId =
            dao.findRemoteId(params.sourceId, role.characterId, SourceDataType.Character)
        val remoteTargetId =
            dao.findRemoteId(params.sourceId, role.targetId, role.targetType.sourceDataType)

        if (remoteCharacterId == null || remoteTargetId == null) return null

        return Triple(remoteCharacterId, remoteTargetId, role.targetType)
    }

    private fun mapper(
        params: SourceParams,
        remote: CharacterRole,
        local: CharacterRole?
    ): CharacterRole {
        if (local != null) {
            return remote.copy(
                id = local.id,
                characterId = local.characterId,
                targetId = local.targetId
            )
        }

        val localCharacterId =
            dao.findLocalId(params.sourceId, remote.characterId, SourceDataType.Character)
        val localTargetId =
            dao.findLocalId(params.sourceId, remote.targetId, remote.targetType.sourceDataType)

        if (localCharacterId == null || localTargetId == null) {
            return remote
        }

        return remote.copy(
            id = 0,
            characterId = localCharacterId,
            targetId = localTargetId
        )
    }

    private fun log(result: ItemSyncerResult<CharacterRole>) {
        logger.i(tag = ItemSyncer.RESULT_TAG) {
            "$type sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}