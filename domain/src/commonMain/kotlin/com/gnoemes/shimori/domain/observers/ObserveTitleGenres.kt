package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.genre.GenreRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTitleGenres(
    private val repository: GenreRepository
) : SubjectInteractor<ObserveTitleGenres.Params, List<Genre>>() {

    override fun create(params: Params): Flow<List<Genre>> =
        repository.observeByTitle(params.id, params.type)

    data class Params(
        val id: Long,
        val type: TrackTargetType,
    )
}