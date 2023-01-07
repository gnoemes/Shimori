package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.CharacterDao
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterRole
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.*
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
        { it.shikimoriId },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    override suspend fun syncRoles(roles: List<CharacterRole>) {
        val time = measureTimeMillis {
            roles.forEach { role ->
                db.characterRoleQueries.insert(
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

    override suspend fun sync(data: List<Character>) {
        val result: ItemSyncerResult<Character>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.characterQueries.queryAll(::character).executeAsList(),
                networkValues = data,
            )
        }

        logger.i(
            "Character sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun sync(
        targetId: Long,
        targetType: RateTargetType,
        data: List<Character>
    ) {
        val result: ItemSyncerResult<Character>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.characterQueries.queryAll(::character).executeAsList(),
                networkValues = data,
            )
        }

        logger.i(
            "Character sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )

        //TODO refactor. Update ids in syncer via transaction with id return
        val addedCharacters =
            db.characterQueries.queryByShikimoriIds(
                result.added.map { it.shikimoriId },
                ::character
            )
                .executeAsList()

        val roles = (addedCharacters + result.updated)
            .map {
                CharacterRole(
                    characterId = it.id,
                    targetId = targetId,
                    targetType = targetType
                )
            }

        syncRoles(roles)
    }

    override suspend fun insert(entity: Character) {
        entity.let {
            db.characterQueries.insert(
                it.shikimoriId,
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
        }
    }

    override suspend fun update(entity: Character) {
        entity.let {
            db.characterQueries.update(
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
                it.description,
                it.descriptionSourceUrl
            )
        }
    }

    override suspend fun delete(entity: Character) {
        db.characterQueries.deleteById(entity.id)
        db.characterRoleQueries.deleteByCharacterId(entity.id)
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
        targetType: RateTargetType
    ): Flow<List<Character>> {
        return db.characterRoleQueries
            .queryByTitle(targetId, targetType, ::character)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCharacterAnimes(id: Long): Flow<List<AnimeWithRate>> {
        return db.characterRoleQueries
            .queryAnimesByCharacter(id, ::animeWithRate)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCharacterMangas(id: Long): Flow<List<MangaWithRate>> {
        return db.characterRoleQueries
            .queryMangasByCharacter(id, ::mangaWithRate)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCharacterRanobes(id: Long): Flow<List<RanobeWithRate>> {
        return db.characterRoleQueries
            .queryRanobesByCharacter(id, ::ranobeWithRate)
            .asFlow()
            .mapToList(dispatchers.io)
            .flowOn(dispatchers.io)
    }
}