package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class CreateOrUpdateTrack(
    private val repository: TrackRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<CreateOrUpdateTrack.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.createOrUpdate(params.track)
        }
    }

    data class Params(
        val track: Track
    )
}