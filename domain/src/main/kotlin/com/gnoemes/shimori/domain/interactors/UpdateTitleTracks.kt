package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateTitleTracks(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitleTracks.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.forceUpdate) {
                update(params.type, params.status)
            } else if (params.optionalUpdate && needUpdate(params.type, params.status)) {
                update(params.type, params.status)
            }
        }
    }

    private suspend fun update(type: TrackTargetType, status: TrackStatus?) = when (type) {
        TrackTargetType.ANIME -> animeRepository.updateMyTitlesByStatus(status)
        TrackTargetType.MANGA -> mangaRepository.updateMyTitlesByStatus(status)
        TrackTargetType.RANOBE -> ranobeRepository.updateMyTitlesByStatus(status)
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