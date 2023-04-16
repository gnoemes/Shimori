package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.CharacterDao
import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterInfo
import com.gnoemes.shimori.data.core.entities.characters.CharacterRole
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.ItemSyncerResult
import com.gnoemes.shimori.data.shared.SYNCER_RESULT_TAG
import com.gnoemes.shimori.data.shared.animeWithTrack
import com.gnoemes.shimori.data.shared.character
import com.gnoemes.shimori.data.shared.mangaWithTrack
import com.gnoemes.shimori.data.shared.ranobeWithTrack
import com.gnoemes.shimori.data.shared.syncRemoteIds
import com.gnoemes.shimori.data.shared.syncerForEntity
import com.gnoemes.shimori.data.shared.withTransaction
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

internal class CharacterDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : CharacterDao() {

    private val syncer = syncerForEntity(
        this,
        { _, character -> character.name.takeIf { it.isNotEmpty() } },
        { _, remote, _ -> remote },
        logger
    )

    override suspend fun insert(sourceId: Long, remote: Character) {
        db.withTransaction {
            remote.let {
                db.characterQueries.insert(
                    it.name,
                    it.nameRu,
                    it.nameEn,
                    it.image?.original,
                    it.image?.preview,
                    it.image?.x96,
                    it.image?.x48,
                    it.url,
                    it.description,
                    it.descriptionSourceUrl
                )
                val localId = characterQueries.selectLastInsertedRowId().executeAsOne()
                syncRemoteIds(sourceId, localId, remote.id, syncDataType)
            }
        }
    }

    override suspend fun update(sourceId: Long, remote: Character, local: Character) {
        db.withTransaction {
            remote.let {
                db.characterQueries.update(
                    local.id,
                    it.name,
                    it.nameRu,
                    it.nameEn,
                    it.image?.original,
                    it.image?.preview,
                    it.image?.x96,
                    it.image?.x48,
                    it.url,
                    it.description,
                    it.descriptionSourceUrl
                )

                syncRemoteIds(sourceId, local.id, remote.id, syncDataType)
            }
        }
    }

    override suspend fun delete(sourceId: Long, local: Character) {
        db.withTransaction {
            characterQueries.deleteById(local.id)
            sourceIdsSyncQueries.deleteByLocal(local.id, syncDataType.type)
        }
    }

    override suspend fun syncRoles(roles: List<CharacterRole>) {
        val time = measureTimeMillis {
            roles.forEach { role ->
                db.characterRoleQueries.upsert(
                    character_id = role.characterId,
                    target_id = role.targetId,
                    target_type = role.targetType
                )
            }
        }

        logger.i(
            "Character roles sync results --> Size: ${roles.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun sync(sourceId: Long, remote: List<Character>) {
        val result: ItemSyncerResult<Character>
        val time = measureTimeMillis {
            result = syncer.sync(
                sourceId = sourceId,
                currentValues = db.characterQueries.queryAll(::character).executeAsList(),
                networkValues = remote,
                removeNotMatched = false
            )
        }

        logger.i(
            "Character sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun sync(
        sourceId: Long,
        targetId: Long,
        targetType: TrackTargetType,
        remote: List<Character>
    ) {
        sync(sourceId, remote)

        remote.mapNotNull { character ->
            val localId = db.sourceIdsSyncQueries
                .findLocalId(sourceId, character.id, syncDataType.type)
                .executeAsOneOrNull() ?: return@mapNotNull null

            CharacterRole(
                characterId = localId,
                targetId = targetId,
                targetType = targetType
            )
        }.also { syncRoles(it) }
    }

    override suspend fun sync(sourceId: Long, characterInfo: CharacterInfo) {
        //save character
        sync(sourceId, arrayListOf(characterInfo.character))

        val localId = db.sourceIdsSyncQueries
            .findLocalId(sourceId, characterInfo.character.id, syncDataType.type)
            .executeAsOneOrNull() ?: return

        //create roles from animes and mangas
        val roles = characterInfo.animes
            .mapNotNull {
                db.sourceIdsSyncQueries
                    .findLocalId(sourceId, it.id, SourceDataType.Anime.type)
                    .executeAsOneOrNull()
            }.map {
                CharacterRole(
                    characterId = localId,
                    targetId = it,
                    targetType = TrackTargetType.ANIME
                )
            } + characterInfo.mangas
            .map {
                val (mangaId, _) = db.sourceIdsSyncQueries
                    .findLocalId(sourceId, it.id, SourceDataType.Manga.type)
                    .executeAsOneOrNull() to false
                if (mangaId == null) {
                    db.sourceIdsSyncQueries
                        .findLocalId(sourceId, it.id, SourceDataType.Ranobe.type)
                        .executeAsOneOrNull() to true
                } else mangaId to false
            }.mapNotNull { (id, isRanobe) ->
                if (id == null) null
                else CharacterRole(
                    characterId = localId,
                    targetId = id,
                    targetType = if (isRanobe) TrackTargetType.RANOBE else TrackTargetType.MANGA
                )
            }

        //finally sync roles
        syncRoles(roles)
    }

    override fun queryById(id: Long): Character? {
        return db.characterQueries.queryById(id, ::character)
            .executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<Character?> {
        return db.characterQueries.queryById(id, ::character)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeByTitle(
        targetId: Long,
        targetType: TrackTargetType
    ): Flow<List<Character>> {
        return db.characterRoleQueries
            .queryByTitle(targetId, targetType, ::character)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCharacterAnimes(id: Long): Flow<List<AnimeWithTrack>> {
        return db.characterRoleQueries
            .queryAnimesByCharacter(id, ::animeWithTrack)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCharacterMangas(id: Long): Flow<List<MangaWithTrack>> {
        return db.characterRoleQueries
            .queryMangasByCharacter(id, ::mangaWithTrack)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCharacterRanobes(id: Long): Flow<List<RanobeWithTrack>> {
        return db.characterRoleQueries
            .queryRanobesByCharacter(id, ::ranobeWithTrack)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }
}