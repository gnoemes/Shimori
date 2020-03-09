package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.app.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAnimeRates @Inject constructor(
    private val animeRepository: AnimeRepository,
    appCoroutineDispatchers: AppCoroutineDispatchers
) : SubjectInteractor<ObserveAnimeRates.Params, List<AnimeWithRate>>() {

    override val dispatcher: CoroutineDispatcher = appCoroutineDispatchers.io

    override fun createObservable(params: Params): Flow<List<AnimeWithRate>> {
        return animeRepository.observeAnimeWithStatus(params.status, params.sort, params.filter)
    }

    data class Params(
        val status: RateStatus,
        val sort: RateSort,
        val filter: String?
    )
}