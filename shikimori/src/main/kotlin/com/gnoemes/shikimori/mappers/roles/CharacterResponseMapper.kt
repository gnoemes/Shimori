package com.gnoemes.shikimori.mappers.roles

import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.roles.CharacterResponse
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class CharacterResponseMapper(
    private val imageMapper: ImageResponseMapper,
) : Mapper<CharacterResponse, Character> {
    override suspend fun map(from: CharacterResponse): Character {
        return Character(
            id = 0,
            shikimoriId = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.name,
            image = imageMapper.map(from.image),
            url = from.url.appendHostIfNeed(),
        )
    }
}