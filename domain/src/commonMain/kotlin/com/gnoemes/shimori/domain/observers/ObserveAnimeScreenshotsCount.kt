package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAnimeScreenshotsCount(
    private val repository: AnimeRepository
) : SubjectInteractor<ObserveAnimeScreenshotsCount.Params, Int>() {

    override fun create(params: Params): Flow<Int> =
        repository.observeScreenshotsCount(
            params.id,
        )

    data class Params(
        val id: Long,
    )
}