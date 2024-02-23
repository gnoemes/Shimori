package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.lists.ListsRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePinExist(
    private val repository: ListsRepository
) : SubjectInteractor<ObservePinExist.Params, Boolean>() {

    override fun create(params: Params): Flow<Boolean> {
        return repository.observePinExist(params.targetId, params.targetType)
    }

    data class Params(
        val targetId: Long,
        val targetType: TrackTargetType
    )
}