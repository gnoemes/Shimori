package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAnimeVideos(
    private val repository: AnimeRepository
) : SubjectInteractor<ObserveAnimeVideos.Params, List<AnimeVideo>>() {

    override fun create(params: Params): Flow<List<AnimeVideo>> = repository.observeVideos(
        params.id,
    )

    data class Params(
        val id: Long,
    )
}