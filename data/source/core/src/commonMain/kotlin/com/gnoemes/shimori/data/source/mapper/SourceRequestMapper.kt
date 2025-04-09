package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SourceArgument
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.sources.SourceIds
import me.tatarka.inject.annotations.Inject

@Inject
class SourceRequestMapper(
    private val dao: SourceIdsSyncDao
) {

    fun <T> toId(
        source: Source,
        data: T
    ): SourceArgument {
        return when (data) {
            is Anime -> findId(source, data.id, SourceDataType.Anime)
            is Manga -> findId(source, data.id, SourceDataType.Manga)
            is Ranobe -> findId(source, data.id, SourceDataType.Ranobe)
            is Track -> findId(source, data.id, SourceDataType.Track)
            is Character -> findId(source, data.id, SourceDataType.Character)
            //TODO remove with user account/user profile separation
            is UserShort -> SourceIdArgument(data.remoteId)
            else -> throw IllegalArgumentException("Data with type ${data!!::class} is not supported")
        }
    }

    private fun findId(source: Source, localId: Long, type: SourceDataType) =
        findUniversalId(source, localId, type)?.let(::MalIdArgument)
            ?: findRemoteId(source, localId, type).let(::SourceIdArgument)


    fun findRemoteId(source: Source, localId: Long, type: SourceDataType): Long {
        return dao.findRemoteId(source.id, localId, type)
            ?: throw IllegalArgumentException("Remote id for $type from source: ${source.name} with id: ${source.id} not found")
    }

    private fun findUniversalId(
        source: Source,
        localId: Long,
        type: SourceDataType
    ): Long? = if (source.supportsMalIds && source.malIdsSupport.contains(type)) {
        dao.findRemoteId(SourceIds.MALUniversal, localId, type)
    } else null


}