package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveExistedStatuses(
    private val repository: TrackRepository
) : SubjectInteractor<ObserveExistedStatuses.Params, List<TrackStatus>>() {

    override fun create(params: Params): Flow<List<TrackStatus>> {
        return repository.observeExistedStatuses(params.type)
    }

    data class Params(
        val type: TrackTargetType
    )
}