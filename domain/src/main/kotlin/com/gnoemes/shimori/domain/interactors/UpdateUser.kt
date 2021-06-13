package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.di.ProcessLifetime
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateUser @Inject constructor(
    private val userRepository: UserRepository,
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime val processScope: CoroutineScope
) : Interactor<UpdateUser.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        if (params.id != null) {
            userRepository.updateUser(params.id)
            return
        }

        if (params.isMe) {
            userRepository.updateMyUser()
        }
    }

    data class Params(
        val id: Long?,
        val isMe: Boolean = false
    )

}