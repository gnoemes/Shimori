package com.gnoemes.shimori.domain.observers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.queryable.QueryableRepository
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveListPage(
    private val animeRepository: AnimeRepository,
    private val queryableRepository: QueryableRepository,
) : PagingInteractor<ObserveListPage.Params, PaginatedEntity>() {

    override fun create(params: Params): Flow<PagingData<PaginatedEntity>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                when (val type = params.type) {
                    TrackTargetType.ANIME -> animeRepository.paging(params.status, params.sort)
                    //show mangas and ranobes in single list
                    TrackTargetType.MANGA, TrackTargetType.RANOBE -> queryableRepository.pagingMangaAndRanobe(
                        params.status,
                        params.sort
                    )

                    else -> throw IllegalArgumentException("List with type $type not supported")
                }
            }
        ).flow
    }


    data class Params(
        val type: TrackTargetType,
        val status: TrackStatus,
        val sort: ListSort,
        override val pagingConfig: PagingConfig
    ) : PagingParams<PaginatedEntity>
}