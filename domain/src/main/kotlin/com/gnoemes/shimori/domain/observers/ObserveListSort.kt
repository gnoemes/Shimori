package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveListSort(
    private val repository: TrackRepository
) : SubjectInteractor<ObserveListSort.Params, ListSort?>() {

    override fun create(params: Params): Flow<ListSort?> {
        return repository.observeListSort(params.type)
    }

    data class Params(
        val type: ListType
    )
}