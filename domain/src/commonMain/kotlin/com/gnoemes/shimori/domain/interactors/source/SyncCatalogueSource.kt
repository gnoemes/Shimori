package com.gnoemes.shimori.domain.interactors.source

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.eventbus.StateBus
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.domain.interactors.UpdateGenres
import com.gnoemes.shimori.domain.interactors.UpdateStudios
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class SyncCatalogueSource(
    private val updateGenres: UpdateGenres,
    private val updateStudios: UpdateStudios,
    private val bus: StateBus,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<SyncCatalogueSource.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            try {
                bus.catalogueSyncActive(true)
                updateGenres(UpdateGenres.Params(params.force)).getOrThrow()
                updateStudios(UpdateStudios.Params(params.force)).getOrThrow()
            } finally {
                bus.catalogueSyncActive(false)
            }

        }
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