package com.gnoemes.shimori.domain.observers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.gnoemes.shimori.data.common.Related
import com.gnoemes.shimori.data.queryable.QueryableRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTitleRelated(
    private val repository: QueryableRepository
) : PagingInteractor<ObserveTitleRelated.Params, Related>() {

    override fun create(params: Params): Flow<PagingData<Related>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                repository.pagingRelated(params.id, params.type)
            }
        ).flow
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType,
        override val pagingConfig: PagingConfig
    ) : PagingParams<Related>
}