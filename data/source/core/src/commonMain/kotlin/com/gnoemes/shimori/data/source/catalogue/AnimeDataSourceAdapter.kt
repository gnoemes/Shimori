package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.source.mapper.SourceAnimeMapper
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.data.source.mapper.SourceTrackStatusMapper
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.AnimeDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SourceIdArgument
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourceAnimeMapper,
    private val statusMapper: SourceTrackStatusMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend AnimeDataSourceAdapter.() -> suspend (AnimeDataSource.(Source) -> ResponseType)
    ): suspend AnimeDataSource.(Source) -> ResponseType {
        val wrap: suspend AnimeDataSourceAdapter.() -> suspend (AnimeDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun get(data: Anime): suspend AnimeDataSource.(Source) -> AnimeInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> get(arg)
            is SourceIdArgument -> get(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }

    fun getCharacters(data: Anime): suspend AnimeDataSource.(Source) -> AnimeInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> getCharacters(arg)
            is SourceIdArgument -> getCharacters(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }

    fun getPersons(data: Anime): suspend AnimeDataSource.(Source) -> AnimeInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> getPersons(arg)
            is SourceIdArgument -> getPersons(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }

    fun getWithStatus(
        user: UserShort?,
        status: TrackStatus?
    ): suspend AnimeDataSource.(Source) -> List<AnimeInfo> = { source ->
        val arg = requestMapper.toId(source, user)
        val statusArg = status?.let { statusMapper(status) }
        when (arg) {
            is SourceIdArgument -> getWithStatus(arg, statusArg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let { mapper.forLists().invoke(it) }
    }

}