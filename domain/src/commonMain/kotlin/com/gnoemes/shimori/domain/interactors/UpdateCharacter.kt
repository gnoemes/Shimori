package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.character.CharacterRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateCharacter(
    private val characterRepository: CharacterRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateCharacter.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) update(params.id)
            else if (characterRepository.shouldUpdateCharacter(params.id)) update(params.id)
        }
    }

    private suspend fun update(id: Long) {
        characterRepository.sync(id)
    }

    data class Params(
        val id: Long,
        val force: Boolean
    ) {
        companion object {
            fun forceUpdate(id: Long) = Params(
                id = id,
                force = true,
            )

            fun optionalUpdate(id: Long) = Params(
                id = id,
                force = false,
            )
        }
    }
}