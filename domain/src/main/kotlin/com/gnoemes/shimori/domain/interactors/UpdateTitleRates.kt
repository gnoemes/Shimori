package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateTitleRates(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitleRates.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.forceUpdate) {
                update(params.type, params.status)
            } else if (params.optionalUpdate && needUpdate(params.type, params.status)) {
                update(params.type, params.status)
            }
        }
    }

    private suspend fun update(type: RateTargetType, status: RateStatus?) = when (type) {
        RateTargetType.ANIME -> animeRepository.updateMyTitlesByStatus(status)
        RateTargetType.MANGA -> mangaRepository.updateMyTitlesByStatus(status)
        RateTargetType.RANOBE -> ranobeRepository.updateMyTitlesByStatus(status)
    }

    private suspend fun needUpdate(type: RateTargetType, status: RateStatus?) = when (type) {
        RateTargetType.ANIME -> animeRepository.needUpdateTitlesWithStatus(status)
        RateTargetType.MANGA -> mangaRepository.needUpdateTitlesWithStatus(status)
        RateTargetType.RANOBE -> ranobeRepository.needUpdateTitlesWithStatus(status)
    }

    data class Params(
        val type: RateTargetType,
        val forceUpdate: Boolean,
        val optionalUpdate: Boolean,
        val status: RateStatus?
    ) {
        companion object {
            fun fullUpdate(type: RateTargetType, status: RateStatus? = null) = Params(
                type = type,
                forceUpdate = true,
                optionalUpdate = false,
                status = null
            )

            fun optionalUpdate(type: RateTargetType, status: RateStatus? = null) = Params(
                type = type,
                forceUpdate = false,
                optionalUpdate = true,
                status = null
            )
        }
    }
}