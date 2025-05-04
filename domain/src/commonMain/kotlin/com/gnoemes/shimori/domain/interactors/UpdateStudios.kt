package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.studio.StudioRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateStudios(
    private val repository: StudioRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateStudios.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) update()
            else if (repository.shouldUpdateStudiosForSource()) update()
        }
    }

    private suspend fun update() {
        repository.sync()
    }

    data class Params(
        val force: Boolean,
    ) {
        companion object {
            fun forceUpdate() = Params(
                force = true,
            )

            fun optionalUpdate() = Params(
                force = false,
            )
        }
    }

}