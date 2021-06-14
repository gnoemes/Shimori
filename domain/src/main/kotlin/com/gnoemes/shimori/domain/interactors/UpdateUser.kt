package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUser @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.id != null) {
                userRepository.updateUser(params.id)
                return@withContext
            }

            if (params.isMe) {
                userRepository.updateMyUser()
            }
        }
    }

    data class Params(
        val id: Long?,
        val isMe: Boolean = false
    )

}