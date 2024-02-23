package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.tracks.TrackRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTracksExist (
    private val repository: TrackRepository
) : SubjectInteractor<Unit, Boolean>() {

    override fun create(params: Unit): Flow<Boolean> {
        return repository.observeTracksExist()
    }
}