package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.anime.AnimeWithRate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAnime @Inject constructor(
    private val animeRepository: AnimeRepository
): SubjectInteractor<ObserveAnime.Params, AnimeWithRate?>() {

    override fun createObservable(params: Params): Flow<AnimeWithRate?> {
        return animeRepository.observeById(params.id)
    }

    data class Params(
        val id: Long
    )
}