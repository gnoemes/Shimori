package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.character.CharacterRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateCharacter(
    private val characterRepository: CharacterRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateCharacter.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) update(params.id)
            else if (characterRepository.needUpdateCharacter(params.id)) update(params.id)
        }
    }

    private suspend fun update(id: Long) = characterRepository.update(id)


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