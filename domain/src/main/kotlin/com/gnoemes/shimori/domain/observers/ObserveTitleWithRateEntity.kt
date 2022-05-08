package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveTitleWithRateEntity(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
) : SubjectInteractor<ObserveTitleWithRateEntity.Params, TitleWithRateEntity?>() {

    override fun create(params: Params): Flow<TitleWithRateEntity?> = when (params.type) {
        RateTargetType.ANIME -> animeRepository.observeById(params.id)
        RateTargetType.MANGA -> mangaRepository.observeById(params.id)
        RateTargetType.RANOBE -> ranobeRepository.observeById(params.id)
    }

    data class Params(
        val id: Long,
        val type: RateTargetType
    )
}