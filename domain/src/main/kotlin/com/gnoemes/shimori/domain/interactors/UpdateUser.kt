package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.source.SourceRepository
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateUser constructor(
    private val sourceRepository: SourceRepository,
    private val userRepository: UserRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.id != null) {
                sourceRepository.trackSources.forEach { source ->
                    try {
                        val (_, user) = sourceRepository.getMyUser(source.id)
                        userRepository.sync(user)
                    } catch (e: Exception) {
                        //retry?
                    }
                }
                return@withContext
            }

            if (params.isMe) {
                sourceRepository.trackSources.forEach { source ->
                    try {
                        val (_, user) = sourceRepository.getMyUser(source.id)
                        userRepository.syncMe(user)
                    } catch (e: Exception) {
                        //retry?
                    }
                }
            }
        }
    }

    data class Params(
        val id: Long?,
        val isMe: Boolean = false
    )

}