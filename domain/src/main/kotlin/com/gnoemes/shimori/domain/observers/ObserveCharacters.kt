package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.repositories.character.CharacterRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveCharacters(
    private val repository: CharacterRepository
) : SubjectInteractor<ObserveCharacters.Params, List<Character>>() {

    override fun create(params: Params): Flow<List<Character>> = repository.observeByTitle(
        params.id,
        params.type
    )

    data class Params(
        val id: Long,
        val type: TrackTargetType,
    )
}