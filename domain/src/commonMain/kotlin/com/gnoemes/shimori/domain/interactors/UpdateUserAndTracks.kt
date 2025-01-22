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
) : Interactor<UpdateUserAndTracks.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            updateUser.invoke(
                UpdateUser.Params(isMe = true, id = null)
            )

            updateTracks.invoke(
                UpdateTracks.Params(
                    forceTitlesUpdate = params.forceTitlesUpdate,
                    optionalTitlesUpdate = params.optionalTitlesUpdate
                )
            )
        }
    }

    data class Params(
        val forceTitlesUpdate: Boolean,
        val optionalTitlesUpdate: Boolean,
    )
}