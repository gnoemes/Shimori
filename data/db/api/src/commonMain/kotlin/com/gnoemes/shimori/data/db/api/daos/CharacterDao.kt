package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

abstract class CharacterDao : EntityDao<Character> {
    abstract fun queryAll(): List<Character>

    abstract fun queryById(id: Long): Character?

    abstract fun observeById(id: Long): Flow<Character?>

    abstract fun observeByTitle(
        targetId: Long,
        targetType: TrackTargetType,
    ): Flow<List<Character>>

    abstract fun observeCharacterAnimes(id: Long): Flow<List<AnimeWithTrack>>
    abstract fun observeCharacterMangas(id: Long): Flow<List<MangaWithTrack>>
    abstract fun observeCharacterRanobes(id: Long): Flow<List<RanobeWithTrack>>
}