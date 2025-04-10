package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.source.mapper.SourceRanobeMapper
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.data.source.mapper.SourceTrackStatusMapper
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.RanobeDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SourceIdArgument
import me.tatarka.inject.annotations.Inject

@Inject
class RanobeDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourceRanobeMapper,
    private val statusMapper: SourceTrackStatusMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend RanobeDataSourceAdapter.() -> suspend (RanobeDataSource.(Source) -> ResponseType)
    ): suspend RanobeDataSource.(Source) -> ResponseType {
        val wrap: suspend RanobeDataSourceAdapter.() -> suspend (RanobeDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun get(data: Ranobe): suspend RanobeDataSource.(Source) -> MangaInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> get(arg)
            is SourceIdArgument -> get(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }

    fun getCharacters(data: Ranobe): suspend RanobeDataSource.(Source) -> MangaInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> getCharacters(arg)
            is SourceIdArgument -> getCharacters(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }

    fun getWithStatus(
        user: UserShort?,
        status: TrackStatus?
    ): suspend RanobeDataSource.(Source) -> List<MangaInfo> = { source ->
        val arg = requestMapper.toId(source, user)
        val statusArg = status?.let { statusMapper(status) }
        when (arg) {
            is SourceIdArgument -> getWithStatus(arg, statusArg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let { mapper.forLists().invoke(it) }
    }

}