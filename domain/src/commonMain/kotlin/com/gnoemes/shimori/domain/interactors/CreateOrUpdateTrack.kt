package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.tracks.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class CreateOrUpdateTrack(
    private val repository: TrackRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<CreateOrUpdateTrack.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.upsert(params.track)
        }
    }

    data class Params(
        val track: Track
    )
}