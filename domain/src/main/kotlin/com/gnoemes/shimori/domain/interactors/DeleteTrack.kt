package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class DeleteTrack(
    private val repository: TrackRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteTrack.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.delete(params.id)
        }
    }

    data class Params(
        val id: Long
    )
}