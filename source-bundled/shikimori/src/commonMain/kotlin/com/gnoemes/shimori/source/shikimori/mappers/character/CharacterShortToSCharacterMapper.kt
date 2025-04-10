package com.gnoemes.shimori.source.shikimori.mappers.character

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SCharacter
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.fragment.CharacterShort
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class CharacterShortToSCharacterMapper(
    private val values: ShikimoriValues
) : Mapper<CharacterShort, SCharacter> {

    override fun map(from: CharacterShort): SCharacter {
        return SCharacter(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.name,
            image = from.poster?.posterShort?.toSourceType(),
            url = from.url.appendHostIfNeed(values),
        )
    }
}