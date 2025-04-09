package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.source.model.SCharacter
import me.tatarka.inject.annotations.Inject

@Inject
class SourceCharacterMapper(
    private val imageMapper: SourceImageMapper,
) : Mapper<SCharacter, CharacterInfo> {

    override fun map(from: SCharacter): CharacterInfo {
        val character = Character(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.nameEn,
            image = from.image?.let { imageMapper.map(it) },
            url = from.url,
            description = from.description,
            descriptionSourceUrl = from.descriptionSourceUrl,
        )
        return CharacterInfo(
            entity = character,
            animes = emptyList(),
            mangas = emptyList()
        )
    }
}