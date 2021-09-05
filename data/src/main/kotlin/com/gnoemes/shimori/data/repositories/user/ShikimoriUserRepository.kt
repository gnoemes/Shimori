package com.gnoemes.shimori.data.repositories.user

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.data_base.sources.UserDataSource
import com.gnoemes.shimori.model.user.User
import com.gnoemes.shimori.model.user.UserShort
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShikimoriUserRepository @Inject constructor(
    private val userStore: UserStore,
    @Shikimori private val userSource: UserDataSource
) {

    fun observeMeShort(): Flow<UserShort?> = userStore.observeMeShort()

    suspend fun getMyUser(): User? {
        return userStore.queryMe()
    }

    suspend fun getMyUserShort(): UserShort? = userStore.queryMeShort()

    suspend fun updateMyUser() {
        val response = userSource.getMyUser()
        if (response is Success) {
            val user = response.data
            userStore.save(user.copy(isMe = true))
        }
    }

    suspend fun updateUser(userId: Long) {
        val response = userSource.getUser(userId)
        if (response is Success) {
            val user = response.data

            val localUser = userStore.queryMe()
            if (localUser != null && localUser.isSameShikimoriUser(user)) {
                userStore.save(user.copy(isMe = true))
                return
            }

            userStore.save(user)
        }
    }

    suspend fun deleteMe() {
        return userStore.deleteMe()
    }
}