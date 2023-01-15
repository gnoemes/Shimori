package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.data.repositories.source.SourceRepository
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateTitleTracks(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository,
    private val sourceRepository: SourceRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitleTracks.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val user = userRepository.queryMeShort(sourceRepository.currentCatalog.id)
            if (user == null || user.isLocal) return@withContext

            if (params.forceUpdate) {
                update(user, params.type, params.status)
            } else if (params.optionalUpdate && needUpdate(params.type, params.status)) {
                update(user, params.type, params.status)
            }
        }
    }

    private suspend fun update(user: UserShort, type: TrackTargetType, status: TrackStatus?) =
        when (type) {
            TrackTargetType.ANIME -> sourceRepository.getTrackedAnimes(user, status)
                .let { (sourceId, data) ->
                    animeRepository.sync(sourceId, data.map { it.entity })
                    trackRepository.sync(sourceId, type, status, data.mapNotNull { it.track })
                    animeRepository.statusUpdated(status)
                }

            TrackTargetType.MANGA -> sourceRepository.getTrackedMangas(user, status)
                .let { (sourceId, data) ->
                    mangaRepository.sync(sourceId, data.map { it.entity })
                    trackRepository.sync(sourceId, type, status, data.mapNotNull { it.track })
                    mangaRepository.statusUpdated(status)
                }
            TrackTargetType.RANOBE -> sourceRepository.getTrackedRanobes(user, status)
                .let { (sourceId, data) ->
                    ranobeRepository.sync(sourceId, data.map { it.entity })
                    trackRepository.sync(sourceId, type, status, data.mapNotNull { it.track })
                    ranobeRepository.statusUpdated(status)
                }
        }

    private suspend fun needUpdate(type: TrackTargetType, status: TrackStatus?) = when (type) {
        TrackTargetType.ANIME -> animeRepository.needUpdateTitlesWithStatus(status)
        TrackTargetType.MANGA -> mangaRepository.needUpdateTitlesWithStatus(status)
        TrackTargetType.RANOBE -> ranobeRepository.needUpdateTitlesWithStatus(status)
    }

    data class Params(
        val type: TrackTargetType,
        val forceUpdate: Boolean,
        val optionalUpdate: Boolean,
        val status: TrackStatus?
    ) {
        companion object {
            fun fullUpdate(type: TrackTargetType, status: TrackStatus? = null) = Params(
                type = type,
                forceUpdate = true,
                optionalUpdate = false,
                status = null
            )

            fun optionalUpdate(type: TrackTargetType, status: TrackStatus? = null) = Params(
                type = type,
                forceUpdate = false,
                optionalUpdate = true,
                status = null
            )
        }
    }
}