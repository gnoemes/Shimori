package com.gnoemes.shimori.data.repositories.source

import com.gnoemes.shimori.base.core.settings.ShimoriStorage
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.core.entities.app.SourceResponse
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.User
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.TrackSource


class SourceRepository(
    private val catalogues: Set<@JvmSuppressWildcards CatalogueSource>,
    private val trackers: Set<@JvmSuppressWildcards TrackSource>,
    private val dao: SourceIdsSyncDao,
    private val preferences: ShimoriStorage,
    private val logger: Logger,
) {

    val currentCatalog: CatalogueSource
        get() {
            val active = preferences.currentCatalogueSource
            return catalogues
                .find { it.name == active }
                ?: catalogues.first().also { preferences.currentCatalogueSource = it.name }
        }

    val trackSources: Set<TrackSource> get() = trackers

    suspend fun getMyUser(sourceId: Long): SourceResponse<User> {
        logger.i("Get user from source: $sourceId")
        return trackSources.find { it.id == sourceId }
            ?.let { source ->
                source.sourceResponse {
                    userDataSource.getMyUser()
                }
            }
            ?: throw IllegalArgumentException("Track source with id $sourceId not found")
    }

    suspend fun createOrUpdateTrack(sourceId: Long, track: Track): SourceResponse<Track> {
        logger.i("Create or update track $track on source: $sourceId")
        val remoteId = dao.findRemoteId(sourceId, track.id, SourceDataType.Track)
        val targetRemoteId =
            dao.findRemoteId(sourceId, track.targetId, track.targetType.sourceDataType)
                ?: throw IllegalArgumentException("Target id for source with id $sourceId not found")
        val sourceTrack = track.copy(
            targetId = targetRemoteId
        )
        return trackSources.find { it.id == sourceId }
            ?.let { source ->
                source.sourceResponse {
                    if (remoteId == null) trackDataSource.create(sourceTrack)
                    else trackDataSource.update(sourceTrack.copy(id = remoteId))
                }

            }
            ?: throw IllegalArgumentException("Track source with id $sourceId not found")
    }

    suspend fun getTracks(sourceId: Long, user: UserShort): SourceResponse<List<Track>> {
        logger.i("Get user tracks from source: $sourceId")
        val remoteId = dao.findRemoteId(sourceId, user.id, SourceDataType.User)
            ?: throw IllegalArgumentException("Remote id for user from source $sourceId not found")
        return trackSources.find { it.id == sourceId }
            ?.let { source -> source.sourceResponse { trackDataSource.getList(user.copy(id = remoteId)) } }
            ?: throw IllegalArgumentException("Track source with id $sourceId not found")
    }

    suspend fun deleteTrack(sourceId: Long, id: Long) {
        val remoteId = dao.findRemoteId(sourceId, id, SourceDataType.Track)
            ?: throw IllegalArgumentException("Remote id for user from source $sourceId not found")
        return trackSources.find { it.id == sourceId }?.trackDataSource?.delete(remoteId)
            ?: throw IllegalArgumentException("Track source with id $sourceId not found")
    }

    suspend fun get(local: Anime) = local.copy(
        id = currentCatalog.findRemoteId(local.id, SourceDataType.Anime)
    ).let { sourceResponse { animeDataSource.get(it).entity } }

    suspend fun get(local: Manga) = local.copy(
        id = currentCatalog.findRemoteId(local.id, SourceDataType.Manga)
    ).let { sourceResponse { mangaDataSource.get(it).entity } }

    suspend fun get(local: Ranobe) = local.copy(
        id = currentCatalog.findRemoteId(local.id, SourceDataType.Ranobe)
    ).let { sourceResponse { ranobeDataSource.get(it).entity } }

    suspend fun get(local: Character) = local.copy(
        id = currentCatalog.findRemoteId(local.id, SourceDataType.Character)
    ).let { sourceResponse { characterDataSource.get(it) } }

    suspend fun getRoles(local: Anime) = local.copy(
        id = currentCatalog.findRemoteId(local.id, SourceDataType.Anime)
    ).let { sourceResponse { animeDataSource.roles(it) } }

    suspend fun getRoles(local: Manga) = local.copy(
        id = currentCatalog.findRemoteId(local.id, SourceDataType.Manga)
    ).let { sourceResponse { mangaDataSource.roles(it) } }

    suspend fun getRoles(local: Ranobe) = local.copy(
        id = currentCatalog.findRemoteId(local.id, SourceDataType.Ranobe)
    ).let { sourceResponse { ranobeDataSource.roles(it) } }

    suspend fun getTrackedAnimes(
        user: UserShort,
        status: TrackStatus?
    ) = sourceResponse { animeDataSource.getWithStatus(user, status) }

    suspend fun getTrackedMangas(
        user: UserShort,
        status: TrackStatus?
    ) = sourceResponse { mangaDataSource.getWithStatus(user, status) }

    suspend fun getTrackedRanobes(
        user: UserShort,
        status: TrackStatus?
    ) = sourceResponse { ranobeDataSource.getWithStatus(user, status) }

    private suspend fun CatalogueSource.findRemoteId(
        localId: Long,
        dataType: SourceDataType
    ): Long {
        return dao.findRemoteId(id, localId, dataType)
            ?: throw IllegalArgumentException("Remote id for $dataType from source $id not found")
    }

    private suspend fun <T> sourceResponse(block: suspend CatalogueSource.() -> T) =
        with(currentCatalog) {
            SourceResponse(
                sourceId = id,
                data = block()
            )
        }

    private suspend fun <T> TrackSource.sourceResponse(block: suspend TrackSource.() -> T) =
        SourceResponse(
            sourceId = id,
            data = block()
        )
}

