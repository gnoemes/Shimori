package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.SCharacterRole
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.AnimeCharactersQuery
import com.gnoemes.shimori.source.shikimori.mappers.character.CharacterShortToSCharacterMapper
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeCharactersMapper(
    private val characterMapper: CharacterShortToSCharacterMapper,
) : Mapper<AnimeCharactersQuery.Anime, SAnime> {

    override fun map(from: AnimeCharactersQuery.Anime): SAnime {
        val characters = from.characterRoles
            ?.map { it.character.characterShort }
            ?.map { characterMapper(it) }

        val characterRoles = from.characterRoles?.map {
            SCharacterRole(
                characterId = it.character.characterShort.id.toLong(),
                targetId = from.id.toLong(),
                targetType = SourceDataType.Anime
            )
        }

        return SAnime(
            id = from.id.toLong(),
            characters = characters,
            charactersRoles = characterRoles
        )
    }
}