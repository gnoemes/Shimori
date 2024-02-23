package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.tracks.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class DeleteTrack(
    private val repository: TrackRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteTrack.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.delete(params.id)
        }
    }

    data class Params(
        val id: Long
    )
}