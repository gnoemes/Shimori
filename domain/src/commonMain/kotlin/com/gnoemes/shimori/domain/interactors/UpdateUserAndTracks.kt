package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateUserAndTracks(
    private val updateUser: UpdateUser,
    private val updateTracks: UpdateTracks,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            updateUser.invoke(
                UpdateUser.Params(isMe = true, id = null)
            )
        }
        withContext(dispatchers.io) {
            updateTracks.invoke(UpdateTracks.Params(true))
        }
    }
}