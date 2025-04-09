package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.source.mapper.SourceMangaMapper
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.data.source.mapper.SourceTrackStatusMapper
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.MangaDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SourceIdArgument
import me.tatarka.inject.annotations.Inject

@Inject
class MangaDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourceMangaMapper,
    private val statusMapper: SourceTrackStatusMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend MangaDataSourceAdapter.() -> suspend (MangaDataSource.(Source) -> ResponseType)
    ): suspend MangaDataSource.(Source) -> ResponseType {
        val wrap: suspend MangaDataSourceAdapter.() -> suspend (MangaDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun get(data: Manga): suspend MangaDataSource.(Source) -> MangaInfo = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is MalIdArgument -> get(arg)
            is SourceIdArgument -> get(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let(mapper::invoke)
    }

    fun getWithStatus(
        user: UserShort?,
        status: TrackStatus?
    ): suspend MangaDataSource.(Source) -> List<MangaInfo> = { source ->
        val arg = requestMapper.toId(source, user)
        val statusArg = status?.let { statusMapper(status) }
        when (arg) {
            is SourceIdArgument -> getWithStatus(arg, statusArg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let { mapper.forLists().invoke(it) }
    }

}