package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterInfo
import com.gnoemes.shimori.data.core.entities.characters.CharacterRole
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

abstract class CharacterDao : SourceSyncEntityDao<Character>(SourceDataType.Character) {
    abstract suspend fun sync(sourceId: Long, remote: List<Character>)
    abstract suspend fun sync(
        sourceId: Long,
        targetId: Long,
        targetType: TrackTargetType,
        remote: List<Character>
    )

    abstract suspend fun sync(sourceId: Long, characterInfo: CharacterInfo)

    abstract suspend fun syncRoles(
        roles: List<CharacterRole>,
    )

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