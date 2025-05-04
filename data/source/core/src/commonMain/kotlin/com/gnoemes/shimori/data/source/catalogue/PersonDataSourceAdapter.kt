package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.person.Person
import com.gnoemes.shimori.data.person.PersonInfo
import com.gnoemes.shimori.data.source.mapper.SourcePersonMapper
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.PersonDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SourceIdArgument
import me.tatarka.inject.annotations.Inject

@Inject
class PersonDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourcePersonMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend PersonDataSourceAdapter.() -> suspend (PersonDataSource.(Source) -> ResponseType)
    ): suspend PersonDataSource.(Source) -> ResponseType {
        val wrap: suspend PersonDataSourceAdapter.() -> suspend (PersonDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun get(data: Person): suspend PersonDataSource.(Source) -> PersonInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> get(arg)
            is SourceIdArgument -> get(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }


}