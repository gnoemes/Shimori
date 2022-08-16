package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateRates(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val rateRepository: RateRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateRates.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.full) {
                //sync titles & rates
                animeRepository.updateMyTitlesByStatus(null)
                mangaRepository.updateMyTitlesByStatus(null)
                ranobeRepository.updateMyTitlesByStatus(null)
            } else {
                // sync only rates
                rateRepository.sync()
            }
        }
    }

    data class Params(
        val full: Boolean
    )
}