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
//            is AnimeInfo -> data.charactersRoles?.let { sync(it) }
//            is MangaInfo -> data.charactersRoles?.let { sync(it) }
            is CharacterInfo -> sync(response.params, data)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
//            data.filterIsInstance<CharacterRole>().isNotEmpty() -> sync(
//                data.filterIsInstance<CharacterRole>()
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

        for (title in remote.animes) {
            val id = dao.findLocalId(params.sourceId, title.id, SourceDataType.Anime) ?: continue

            roles += CharacterRole(
                characterId = characterId,
                targetId = id,
                targetType = TrackTargetType.ANIME
            )
        }

        for (title in remote.mangas) {
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


        sync(characterId, roles)
    }

    private fun sync(characterId: Long, remote: List<CharacterRole>) {
        val result = createSyncer().sync(
            currentValues = characterRoleDao.queryByCharacterId(characterId),
            networkValues = remote,
            removeNotMatched = true
        )

        log(result)
    }

    private fun sync(params: SourceParams, remote: List<CharacterRole>) {
        remote.groupBy { it.characterId }
            .forEach { entry ->
                val characterId = dao.findLocalId(
                    params.sourceId,
                    entry.key,
                    SourceDataType.Character
                ) ?: return

                val result = createSyncer().sync(
                    currentValues = characterRoleDao.queryByCharacterId(characterId),
                    networkValues = entry.value,
                    removeNotMatched = true
                )

                log(result)
            }
    }

    private fun createSyncer() = syncerForEntity(
        characterRoleDao,
        { role -> Triple(role.characterId, role.targetId, role.targetType) },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<CharacterRole>) {
        logger.i(tag = ItemSyncer.RESULT_TAG) {
            "$type sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}