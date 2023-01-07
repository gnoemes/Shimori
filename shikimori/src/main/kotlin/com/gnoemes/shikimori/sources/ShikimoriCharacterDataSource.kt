package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.roles.CharacterDetailsResponseMapper
import com.gnoemes.shikimori.mappers.roles.CharacterResponseMapper
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterInfo
import com.gnoemes.shimori.data.core.sources.CharacterDataSource

internal class ShikimoriCharacterDataSource(
    private val shikimori: Shikimori,
    private val mapper: CharacterResponseMapper,
    private val detailsMapper: CharacterDetailsResponseMapper
) : CharacterDataSource {

    override suspend fun get(character: Character): CharacterInfo {
        return shikimori.character.getDetails(character.shikimoriId)
            .let { detailsMapper.map(it) }
    }
}