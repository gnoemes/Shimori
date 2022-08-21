package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateUser constructor(
    private val userRepository: ShikimoriUserRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.id != null) {
                userRepository.update(params.id)
                return@withContext
            }

            if (params.isMe) {
                userRepository.updateMe()
            }
        }
    }

    data class Params(
        val id: Long?,
        val isMe: Boolean = false
    )

}