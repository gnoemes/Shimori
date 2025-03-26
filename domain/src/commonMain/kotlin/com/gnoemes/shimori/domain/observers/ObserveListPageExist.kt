package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.queryable.QueryableRepository
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveListPageExist(
    private val animeRepository: AnimeRepository,
    private val queryableRepository: QueryableRepository,
) : SubjectInteractor<ObserveListPageExist.Params, Boolean>() {

    override fun create(params: Params): Flow<Boolean> {
        return when (val type = params.type) {
            TrackTargetType.ANIME -> animeRepository.observeStatusExists(params.status)
            //show mangas and ranobes in single list
            TrackTargetType.MANGA, TrackTargetType.RANOBE -> queryableRepository.observeStatusExists(
                params.status,
            )

            else -> throw IllegalArgumentException("List with type $type not supported")
        }
    }


    data class Params(
        val type: TrackTargetType,
        val status: TrackStatus,
    )
}