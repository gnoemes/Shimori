package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.source.track.TrackManager
import com.gnoemes.shimori.data.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateUser(
    private val trackManager: TrackManager,
    private val userRepository: UserRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateUser.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.id != null) {
                trackManager.trackers.forEach { source ->
                    val user = trackManager.user(source.id) {
                        get(params.id)
                    }
                    userRepository.trySync(user)
                }
                return@withContext
            }

            if (params.isMe) {
                trackManager.trackers.forEach { source ->
                    val user = trackManager.user(source.id) {
                        getMyUser()
                    }
                    userRepository.trySync(user.copy(data = user.data.copy(isMe = true)))
                }
            }
        }
    }

    data class Params(
        val id: Long?,
        val isMe: Boolean = false
    )

}