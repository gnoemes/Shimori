package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveTracksExist constructor(
    private val repository: TrackRepository
) : SubjectInteractor<Unit, Boolean>() {

    override fun create(params: Unit): Flow<Boolean> {
        return repository.observeTracksExist()
    }
}