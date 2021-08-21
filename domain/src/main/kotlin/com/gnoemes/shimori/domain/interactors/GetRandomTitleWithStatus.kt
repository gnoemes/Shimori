package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.ResultInteractor
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRandomTitleWithStatus @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetRandomTitleWithStatus.Params, EntityWithRate<out ShimoriEntity>?>() {

    override suspend fun doWork(params: Params): EntityWithRate<out ShimoriEntity>? =
        withContext(dispatchers.io) {
            when (params.type) {
                ListType.Anime -> animeRepository.queryRandomByStatus(params.status)
                ListType.Manga -> mangaRepository.queryRandomByStatus(params.status)
                ListType.Ranobe -> ranobeRepository.queryRandomByStatus(params.status)
                else -> throw IllegalArgumentException("${params.type} is not supported")
            }
        }

    data class Params(
        val type: ListType,
        val status: RateStatus?,
    )
}