package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateUserAndRates constructor(
    private val updateUser: UpdateUser,
    private val updateRates: UpdateRates,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            updateUser.executeSync(
                UpdateUser.Params(isMe = true, id = null)
            )
        }
        withContext(dispatchers.io) {
            updateRates.executeSync(Unit)
        }
    }
}