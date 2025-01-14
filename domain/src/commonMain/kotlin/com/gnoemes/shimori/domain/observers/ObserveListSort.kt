package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.lists.ListsRepository
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveListSort(
    private val repository: ListsRepository
) : SubjectInteractor<ObserveListSort.Params, ListSort?>() {

    override fun create(params: Params): Flow<ListSort?> {
        return repository.observeListSort(params.type)
    }

    data class Params(
        val type: ListType
    )
}