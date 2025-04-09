package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.data.source.mapper.SourceCharacterMapper
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.CharacterDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SourceIdArgument
import me.tatarka.inject.annotations.Inject

@Inject
class CharacterDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourceCharacterMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend CharacterDataSourceAdapter.() -> suspend (CharacterDataSource.(Source) -> ResponseType)
    ): suspend CharacterDataSource.(Source) -> ResponseType {
        val wrap: suspend CharacterDataSourceAdapter.() -> suspend (CharacterDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun get(data: Character): suspend CharacterDataSource.(Source) -> CharacterInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> get(arg)
            is SourceIdArgument -> get(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }


}