package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.lists.ListsRepository
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveListSort(
    private val repository: ListsRepository
) : SubjectInteractor<ObserveListSort.Params, ListSort>() {

    override fun create(params: Params): Flow<ListSort> {
        return repository.observeListSort(
            //mangas and ranobes are shown on single list
            //so count ranobe as manga
            params.type.let { if (it.ranobe) TrackTargetType.MANGA else it }
        )
            .mapNotNull { it ?: ListSort.defaultForType(params.type) }
    }

    data class Params(
        val type: TrackTargetType
    )
}