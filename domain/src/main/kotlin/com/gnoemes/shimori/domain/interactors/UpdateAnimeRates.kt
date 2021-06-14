package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateAnimeRates @Inject constructor(
    private val repository: AnimeRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateAnimeRates.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) {
                repository.updateMyAnimeWithStatus(params.status)
            } else if (params.categorySize != null) {
                val animes = repository.queryAnimesWithStatus(params.status)

                //refresh if local animes diffs with rate category size
                if (animes.size != params.categorySize) {
                    repository.updateMyAnimeWithStatus(params.status)
                }
            }
        }
    }

    data class Params(
        val force: Boolean,
        val status: RateStatus,
        val categorySize: Int?
    )
}