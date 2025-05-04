package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.studio.StudioRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAnimeStudios(
    private val repository: StudioRepository
) : SubjectInteractor<ObserveAnimeStudios.Params, List<Studio>>() {

    override fun create(params: Params): Flow<List<Studio>> = repository.observeByTitle(
        params.id,
    )

    data class Params(
        val id: Long,
    )
}