package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteMyUser @Inject constructor(
    private val userRepository: ShikimoriUserRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            userRepository.deleteMe()
        }
    }
}