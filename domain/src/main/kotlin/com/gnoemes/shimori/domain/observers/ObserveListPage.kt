package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.paging.Pager
import com.gnoemes.shimori.data.paging.PagingConfig
import com.gnoemes.shimori.data.paging.PagingData
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow

class ObserveListPage(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val listPinRepository: ListPinRepository,
) : PagingInteractor<ObserveListPage.Params, PaginatedEntity>() {

    override fun create(params: Params): Flow<PagingData<PaginatedEntity>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                when (val type = params.type) {
                    ListType.Anime -> animeRepository.paging(params.status, params.sort)
                    ListType.Manga -> mangaRepository.paging(params.status, params.sort)
                    ListType.Ranobe -> ranobeRepository.paging(params.status, params.sort)
                    ListType.Pinned -> listPinRepository.paging(params.sort)
                    else -> throw IllegalArgumentException("List with type $type not supported")
                }
            }
        ).pagingData
    }


    data class Params(
        val type: ListType,
        val status: TrackStatus,
        val sort: ListSort,
        override val pagingConfig: PagingConfig
    ) : PagingParams<PaginatedEntity>
}