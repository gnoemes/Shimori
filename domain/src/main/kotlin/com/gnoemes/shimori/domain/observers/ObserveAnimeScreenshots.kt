package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveAnimeScreenshots(
    private val repository: AnimeRepository
) : SubjectInteractor<ObserveAnimeScreenshots.Params, List<AnimeScreenshot>>() {

    override fun create(params: Params): Flow<List<AnimeScreenshot>> =
        repository.observeScreenshots(
            params.id,
        )

    data class Params(
        val id: Long,
    )
}