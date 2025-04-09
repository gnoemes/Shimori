package com.gnoemes.shimori.data.source.track

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.data.source.mapper.SourceTrackMapper
import com.gnoemes.shimori.data.source.mapper.SourceTrackStatusMapper
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.track.TrackDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class TrackDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourceTrackMapper,
    private val statusMapper: SourceTrackStatusMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend TrackDataSourceAdapter.() -> suspend (TrackDataSource.(Source) -> ResponseType)
    ): suspend TrackDataSource.(Source) -> ResponseType {
        val wrap: suspend TrackDataSourceAdapter.() -> suspend (TrackDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun create(data: Track): suspend TrackDataSource.(Source) -> Track = { source ->
        val arg = when (val idArgument = requestMapper.toId(source, data)) {
            is SourceIdArgument -> mapper.mapInverse(data.copy(id = idArgument.id))
            else -> throw IllegalArgumentException("Unknown argument $idArgument for request")
        }

        create(arg).let(mapper::map)
    }

    fun update(data: Track): suspend TrackDataSource.(Source) -> Track = { source ->
        val arg = when (val idArgument = requestMapper.toId(source, data)) {
            is SourceIdArgument -> mapper.mapInverse(data.copy(id = idArgument.id))
            else -> throw IllegalArgumentException("Unknown argument $idArgument for request")
        }

        update(arg).let(mapper::map)
    }

    fun delete(data: Long): suspend TrackDataSource.(Source) -> Unit = {
        delete(SourceIdArgument(data))
    }

    fun getList(data: UserShort): suspend TrackDataSource.(Source) -> List<Track> = { source ->
        val arg = requestMapper.toId(source, data)
        when (arg) {
            is SourceIdArgument -> getList(arg)
            else -> throw IllegalArgumentException("Unknown argument $arg for request")
        }.let { mapper.forLists().invoke(it) }
    }
}