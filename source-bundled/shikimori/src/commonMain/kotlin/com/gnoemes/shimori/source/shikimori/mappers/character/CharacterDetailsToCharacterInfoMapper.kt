package com.gnoemes.shimori.source.shikimori.mappers.character

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SCharacter
import com.gnoemes.shimori.source.shikimori.CharacterDetailsQuery
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class CharacterDetailsToCharacterInfoMapper(
    private val values: ShikimoriValues,
) : Mapper<CharacterDetailsQuery.Character, SCharacter> {

    override fun map(from: CharacterDetailsQuery.Character): SCharacter {
        return SCharacter(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.japanese,
            image = from.poster?.posterShort?.toSourceType(),
            url = from.url.appendHostIfNeed(values),
            description = from.description,
            descriptionSourceUrl = from.descriptionSource,
        )
    }
}