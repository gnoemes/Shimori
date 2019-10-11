package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.search.SearchRepository
import com.gnoemes.shimori.domain.SuspendingWorkInteractor
import com.gnoemes.shimori.model.anime.Anime
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SearchPaginated @Inject constructor(
    private val searchRepository: SearchRepository,
    dispatchers: AppCoroutineDispatchers
) : SuspendingWorkInteractor<SearchPaginated.Params, List<Anime>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun doWork(params: Params): List<Anime> {
        return searchRepository.search()
    }

    data class Params(val filters: Map<String, String>)
}