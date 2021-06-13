package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.di.ProcessLifetime
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateAnimeRates @Inject constructor(
    private val repository: AnimeRepository,
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime val processScope: CoroutineScope
) : Interactor<UpdateAnimeRates.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
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

    data class Params(
        val force: Boolean,
        val status: RateStatus,
        val categorySize: Int?
    )
}