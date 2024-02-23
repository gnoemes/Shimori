package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.user.UserShort
import me.tatarka.inject.annotations.Inject

@Suppress("UNCHECKED_CAST")
@Inject
class SourceRequestMapper(
    private val dao: SourceIdsSyncDao
) {

    operator fun<T> invoke(sourceId: Long, type: SourceDataType, data: T): T {
        return when (data) {
            is Anime -> data.copy(id = findRemoteId(sourceId, data.id, type)) as T
            is Manga -> data.copy(id = findRemoteId(sourceId, data.id, type)) as T
            is Ranobe -> data.copy(id = findRemoteId(sourceId, data.id, type)) as T
            is Track -> data.copy(id = findRemoteId(sourceId, data.id, type)) as T
            //user always contains remote id
            is UserShort -> data
            else -> throw IllegalArgumentException("Data with type ${data!!::class} is not supported")
        }
    }

    fun findRemoteId(sourceId: Long, localId: Long, type: SourceDataType): Long {
        return dao.findRemoteId(sourceId, localId, type)
            ?: throw IllegalArgumentException("Remote id for $type from source with id: $sourceId not found")
    }

}