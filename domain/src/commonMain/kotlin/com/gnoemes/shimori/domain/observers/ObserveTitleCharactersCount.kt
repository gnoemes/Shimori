package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.character.CharacterRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTitleCharactersCount(
    private val repository: CharacterRepository
) : SubjectInteractor<ObserveTitleCharactersCount.Params, Int>() {

    override fun create(params: Params): Flow<Int> {
        return repository.observeTitleCharactersCount(params.id, params.type)
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType,
    )
}