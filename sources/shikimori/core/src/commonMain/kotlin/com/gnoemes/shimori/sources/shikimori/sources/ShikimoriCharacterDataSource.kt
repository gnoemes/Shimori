package com.gnoemes.shimori.sources.shikimori.sources

import com.apollographql.apollo3.api.Optional
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.source.data.CharacterDataSource
import com.gnoemes.shimori.sources.shikimori.CharacterDetailsQuery
import com.gnoemes.shimori.sources.shikimori.ShikimoriApi
import com.gnoemes.shimori.sources.shikimori.mappers.character.CharacterDetailsToCharacterInfoMapper

class ShikimoriCharacterDataSource(
    private val api: ShikimoriApi,
    private val mapper : CharacterDetailsToCharacterInfoMapper,
) : CharacterDataSource {

    override suspend fun get(character: Character): CharacterInfo {
        return api.character.graphql(
            CharacterDetailsQuery(
                ids = Optional.present(listOf(character.id.toString()))
            )
        ).dataAssertNoErrors
            .characters
            .first()
            .let { mapper.map(it) }
    }
}