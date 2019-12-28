package com.gnoemes.shimori.data.repositories.user

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.asyncOrAwait
import com.gnoemes.shimori.data_base.sources.UserDataSource
import com.gnoemes.shimori.model.user.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userStore: UserStore,
    @Shikimori private val userSource: UserDataSource
) {

    suspend fun getMyUser(): User? {
        return userStore.queryMe()
    }

    suspend fun getMyUserId(): Long? {
        return userStore.queryMyId()
    }

    suspend fun updateMyUser() {
        asyncOrAwait("update_user_my") {
            val response = userSource.getMyUser()
            if (response is Success) {
                val user = response.data
                userStore.save(user.copy(isMe = true))
            }
        }
    }

    suspend fun updateUser(userId: Long) {
        asyncOrAwait("update_user_$userId") {
            val response = userSource.getUser(userId)
            if (response is Success) {
                val user = response.data

                val myUserId = userStore.queryMyId()
                if (myUserId != null && user.shikimoriId == myUserId) {
                    userStore.save(user.copy(isMe = true))
                    return@asyncOrAwait
                }

                userStore.save(user)
            }
        }
    }
}