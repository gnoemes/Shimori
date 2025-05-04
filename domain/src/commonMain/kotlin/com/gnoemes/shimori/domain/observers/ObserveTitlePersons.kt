package com.gnoemes.shimori.domain.observers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.gnoemes.shimori.data.person.Person
import com.gnoemes.shimori.data.person.PersonRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTitlePersons(
    private val repository: PersonRepository
) : PagingInteractor<ObserveTitlePersons.Params, Person>() {

    override fun create(params: Params): Flow<PagingData<Person>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                repository.observeTitlePersons(params.id, params.type)
            }
        ).flow
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType,
        override val pagingConfig: PagingConfig
    ) : PagingParams<Person>
}