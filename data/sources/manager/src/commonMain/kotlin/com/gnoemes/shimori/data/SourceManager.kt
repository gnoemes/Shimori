package com.gnoemes.shimori.data

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.TrackSource
import com.gnoemes.shimori.source.data.AnimeDataSource
import com.gnoemes.shimori.source.data.CharacterDataSource
import com.gnoemes.shimori.source.data.MangaDataSource
import com.gnoemes.shimori.source.data.RanobeDataSource
import com.gnoemes.shimori.source.data.TrackDataSource
import com.gnoemes.shimori.source.data.UserDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class SourceManager(
    private val catalogs: Set<CatalogueSource>,
    private val trackers: Set<TrackSource>,
    private val prefs: ShimoriPreferences,
    private val dao: SourceIdsSyncDao,
) {
    private val currentCatalog: CatalogueSource
        get() {
            val active = prefs.currentCatalogueSource
            return catalogs
                .find { it.name == active }
                ?: catalogs.first().also { prefs.currentCatalogueSource = it.name }
        }

    suspend fun <T> anime(block: suspend AnimeDataSource.(findRemoteId: suspend (localId: Long) -> Long) -> T): SourceResponse<T> =
        response { block(animeDataSource) { findRemoteId(it, SourceDataType.Anime) } }

    suspend fun <T> manga(block: suspend MangaDataSource.(findRemoteId: suspend (localId: Long) -> Long) -> T): SourceResponse<T> =
        response { block(mangaDataSource) { findRemoteId(it, SourceDataType.Manga) } }

    suspend fun <T> ranobe(block: suspend RanobeDataSource.(findRemoteId: suspend (localId: Long) -> Long) -> T): SourceResponse<T> =
        response { block(ranobeDataSource) { findRemoteId(it, SourceDataType.Ranobe) } }

    suspend fun <T> character(block: suspend CharacterDataSource.(findRemoteId: suspend (localId: Long) -> Long) -> T): SourceResponse<T> =
        response { block(characterDataSource) { findRemoteId(it, SourceDataType.Character) } }

    suspend fun <T> user(
        sourceId: Long,
        block: suspend UserDataSource.(findRemoteId: suspend (localId: Long) -> Long) -> T
    ): SourceResponse<T> =
        trackers[sourceId]?.response {
            block(userDataSource) {
                findRemoteId(
                    it,
                    SourceDataType.User
                )
            }
        }
            ?: throw IllegalArgumentException("Track source with id $sourceId not found")

    suspend fun <T> track(
        sourceId: Long,
        block: suspend TrackDataSource.(findRemoteId: suspend (localId: Long) -> Long) -> T
    ): SourceResponse<T> =
        trackers[sourceId]?.response {
            block(trackDataSource) {
                findRemoteId(
                    it,
                    SourceDataType.Track
                )
            }
        }
            ?: throw IllegalArgumentException("Track source with id $sourceId not found")

    operator fun Set<CatalogueSource>.get(id: Long) = find { it.id == id }
    operator fun Set<TrackSource>.get(id: Long) = find { it.id == id }

    private fun CatalogueSource.findRemoteId(
        localId: Long,
        dataType: SourceDataType
    ): Long {
        return dao.findRemoteId(id, localId, dataType)
            ?: throw IllegalArgumentException("Remote id for $dataType from source $id not found")
    }

    private fun TrackSource.findRemoteId(
        localId: Long,
        dataType: SourceDataType
    ): Long {
        return dao.findRemoteId(id, localId, dataType)
            ?: throw IllegalArgumentException("Remote id for $dataType from source $id not found")
    }

    private suspend fun <T> response(block: suspend CatalogueSource.() -> T) =
        with(currentCatalog) {
            SourceResponse(
                sourceId = id,
                data = block()
            )
        }

    private suspend fun <T> TrackSource.response(block: suspend TrackSource.() -> T) =
        SourceResponse(
            sourceId = id,
            data = block()
        )
}