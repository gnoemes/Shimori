package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.lists.ListsRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class ToggleTitlePin(
    private val repository: ListsRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<ToggleTitlePin.Params, Boolean>() {

    override suspend fun doWork(params: Params): Boolean {
        return withContext(dispatchers.io) {
            if (params.pin != null) {
                repository.pin(params.id, params.type, params.pin)
            } else {
                repository.togglePin(params.id, params.type)
            }
        }
    }

    data class Params(
        val type: TrackTargetType,
        val id: Long,
        val pin: Boolean? = null
    )
}