package com.gnoemes.shimori.sources.shikimori.mappers.character

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.sources.shikimori.CharacterDetailsQuery
import com.gnoemes.shimori.sources.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriImage
import me.tatarka.inject.annotations.Inject

@Inject
class CharacterDetailsToCharacterInfoMapper(
    private val values: ShikimoriValues,
) : Mapper<CharacterDetailsQuery.Character, CharacterInfo> {

    override fun map(from: CharacterDetailsQuery.Character): CharacterInfo {

        val character = Character(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.japanese,
            image = from.poster?.posterShort?.toShimoriImage(),
            url = from.url.appendHostIfNeed(values),
            description = from.description,
            descriptionSourceUrl = from.descriptionSource
        )

        return CharacterInfo(
            entity = character,
            //TODO change graphql to rest for character details
            animes = emptyList(),
            mangas = emptyList(),
        )
    }
}