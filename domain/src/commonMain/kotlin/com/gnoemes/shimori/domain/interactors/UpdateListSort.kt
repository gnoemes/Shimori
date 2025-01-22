package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.lists.ListsRepository
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateListSort(
    private val repository: ListsRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateListSort.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.upsert(params.sort)
        }
    }

    data class Params(
        val sort: ListSort,
    )
}