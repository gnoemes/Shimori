package com.gnoemes.shimori.source.shikimori.sources

import com.apollographql.apollo.api.Optional
import com.gnoemes.shimori.source.catalogue.CharacterDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SCharacter
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.shikimori.CharacterDetailsQuery
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.character.CharacterDetailsToCharacterInfoMapper
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriCharacterDataSource(
    private val api: ShikimoriApi,
    private val mapper: CharacterDetailsToCharacterInfoMapper,
) : CharacterDataSource {

    override suspend fun get(id: MalIdArgument) = get(SourceIdArgument(id))

    override suspend fun get(id: SourceIdArgument): SCharacter {
        return api.apollo.query(
            CharacterDetailsQuery(
                ids = Optional.present(listOf(id.id.toString()))
            )
        ).dataAssertNoErrors
            .characters
            .first()
            .let { mapper.map(it) }
    }

}