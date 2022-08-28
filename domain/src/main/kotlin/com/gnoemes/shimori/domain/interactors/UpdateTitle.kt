package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
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
            } else if (needUpdate(params.id, params.type)) {
                update(params.id, params.type)
            }
        }
        //TODO update characters & other related content
    }

    private suspend fun update(id: Long, type: RateTargetType) = when (type) {
        RateTargetType.ANIME -> animeRepository.update(id)
        RateTargetType.MANGA -> mangaRepository.update(id)
        RateTargetType.RANOBE -> ranobeRepository.update(id)
    }

    private suspend fun needUpdate(id: Long, type: RateTargetType) = when (type) {
        RateTargetType.ANIME -> animeRepository.needUpdateTitle(id)
        RateTargetType.MANGA -> mangaRepository.needUpdateTitle(id)
        RateTargetType.RANOBE -> ranobeRepository.needUpdateTitle(id)
    }

    data class Params(
        val id: Long,
        val type: RateTargetType,
        val force: Boolean,
        //TODO add support for external ids (shikimori, mal, ...)
    ) {
        companion object {
            fun forceUpdate(id: Long, type: RateTargetType) = Params(
                id = id,
                type = type,
                force = true,
            )

            fun optionalUpdate(id: Long, type: RateTargetType) = Params(
                id = id,
                type = type,
                force = false,
            )
        }
    }
}