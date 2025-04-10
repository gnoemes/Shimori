package com.gnoemes.shimori.source.shikimori.mappers.ranobe

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.source.model.SCharacterRole
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.MangaCharactersQuery
import com.gnoemes.shimori.source.shikimori.mappers.character.CharacterShortToSCharacterMapper
import me.tatarka.inject.annotations.Inject

@Inject
class RanobeCharactersMapper(
    private val characterMapper: CharacterShortToSCharacterMapper,
) : Mapper<MangaCharactersQuery.Manga, SManga> {

    override fun map(from: MangaCharactersQuery.Manga): SManga {
        val characters = from.characterRoles
            ?.map { it.character.characterShort }
            ?.map { characterMapper(it) }

        val characterRoles = from.characterRoles?.map {
            SCharacterRole(
                characterId = it.character.characterShort.id.toLong(),
                targetId = from.id.toLong(),
                targetType = SourceDataType.Ranobe,
                role = it.rolesEn.firstOrNull(),
                roleRu = it.rolesRu.firstOrNull()
            )
        }

        return SManga(
            id = from.id.toLong(),
            type = SourceDataType.Ranobe,
            characters = characters,
            charactersRoles = characterRoles
        )
    }
}