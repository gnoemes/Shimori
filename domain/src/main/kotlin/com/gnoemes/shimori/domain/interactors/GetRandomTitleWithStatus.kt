package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.ResultInteractor
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRandomTitleWithStatus @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetRandomTitleWithStatus.Params, EntityWithRate<out ShimoriEntity>?>() {

    override suspend fun doWork(params: Params): EntityWithRate<out ShimoriEntity>? =
        withContext(dispatchers.io) {
            when (params.type) {
                RateTargetType.ANIME -> animeRepository.queryRandomAnimeWithStatus(params.status)
                else -> throw IllegalArgumentException("${params.type} is not supported")
            }
        }

    data class Params(
        val type: RateTargetType,
        val status: RateStatus?,
    )
}