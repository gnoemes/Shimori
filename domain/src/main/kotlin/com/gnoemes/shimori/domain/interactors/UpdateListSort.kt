package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateListSort(
    private val repository: TrackRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateListSort.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val sort = ListSort(
                type = params.type,
                sortOption = params.sort,
                isDescending = params.descending
            )
            repository.createOrUpdate(sort)
        }
    }

    data class Params(
        val type: ListType,
        val sort: ListSortOption,
        val descending: Boolean
    )
}