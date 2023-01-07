package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterRole
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import kotlinx.coroutines.flow.Flow

abstract class CharacterDao : EntityDao<Character>() {

    abstract suspend fun syncRoles(
        roles: List<CharacterRole>,
    )

    abstract suspend fun sync(data: List<Character>)
    abstract suspend fun sync(
        targetId: Long,
        targetType: RateTargetType,
        data: List<Character>
    )

    abstract fun queryById(id: Long): Character?

    abstract fun observeById(id: Long): Flow<Character?>

    abstract fun observeByTitle(
        targetId: Long,
        targetType: RateTargetType,
    ): Flow<List<Character>>

    abstract fun observeCharacterAnimes(id: Long): Flow<List<AnimeWithRate>>
    abstract fun observeCharacterMangas(id: Long): Flow<List<MangaWithRate>>
    abstract fun observeCharacterRanobes(id: Long): Flow<List<RanobeWithRate>>
}