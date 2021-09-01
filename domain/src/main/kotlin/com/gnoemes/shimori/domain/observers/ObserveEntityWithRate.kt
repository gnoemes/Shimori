package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject

class ObserveEntityWithRate @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
) : SubjectInteractor<ObserveEntityWithRate.Params, EntityWithRate<out ShimoriEntity>?>() {

    override fun createObservable(params: Params) = when (params.type) {
        RateTargetType.ANIME -> animeRepository.observeById(params.id)
        RateTargetType.MANGA -> mangaRepository.observeById(params.id)
        RateTargetType.RANOBE -> ranobeRepository.observeById(params.id)
    }

    data class Params(
        val id: Long,
        val type: RateTargetType
    )
}