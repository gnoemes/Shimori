package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateTitle(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitle.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) {
                update(params.id, params.type)
                updateRoles(params.id, params.type)
            } else {
                if (needUpdate(params.id, params.type)) update(params.id, params.type)
                if (needUpdateRoles(params.id, params.type)) updateRoles(params.id, params.type)
            }
        }
        //TODO other related content
    }

    private suspend fun update(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.update(id)
        TrackTargetType.MANGA -> mangaRepository.update(id)
        TrackTargetType.RANOBE -> ranobeRepository.update(id)
    }

    private suspend fun needUpdate(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.needUpdateTitle(id)
        TrackTargetType.MANGA -> mangaRepository.needUpdateTitle(id)
        TrackTargetType.RANOBE -> ranobeRepository.needUpdateTitle(id)
    }

    private suspend fun updateRoles(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.updateRoles(id)
        TrackTargetType.MANGA -> mangaRepository.updateRoles(id)
        TrackTargetType.RANOBE -> ranobeRepository.updateRoles(id)
    }

    private suspend fun needUpdateRoles(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.needUpdateTitleRoles(id)
        TrackTargetType.MANGA -> mangaRepository.needUpdateTitleRoles(id)
        TrackTargetType.RANOBE -> ranobeRepository.needUpdateTitleRoles(id)
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType,
        val force: Boolean,
        //TODO add support for external ids (shikimori, mal, ...)
    ) {
        companion object {
            fun forceUpdate(id: Long, type: TrackTargetType) = Params(
                id = id,
                type = type,
                force = true,
            )

            fun optionalUpdate(id: Long, type: TrackTargetType) = Params(
                id = id,
                type = type,
                force = false,
            )
        }
    }
}