package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.db.api.daos.CharacterDao
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.animeWithTrack
import com.gnoemes.shimori.data.util.character
import com.gnoemes.shimori.data.util.mangaWithTrack
import com.gnoemes.shimori.data.util.ranobeWithTrack
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class CharacterDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : CharacterDao(), SqlDelightEntityDao<Character> {
    override fun insert(entity: Character): Long {
        entity.let {
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
        }

        return db.characterQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Character) {
        entity.let {
            db.characterQueries.update(
                it.id,
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

    override fun delete(entity: Character) {
        return db.characterQueries.deleteById(entity.id)
    }

    override fun queryAll(): List<Character> {
        return db.characterQueries.queryAll(::character).executeAsList()
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