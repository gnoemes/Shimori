package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.character.CharacterRepository
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
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