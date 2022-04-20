package com.gnoemes.shimori.data.base.database.daos

import com.gnoemes.shimori.data.base.entities.user.User
import com.gnoemes.shimori.data.base.entities.user.UserShort
import kotlinx.coroutines.flow.Flow

abstract class UserDao : EntityDao<User>() {
    abstract fun observeMeShort(): Flow<UserShort?>

    abstract suspend fun queryMe(): User?
    abstract suspend fun queryMeShort(): UserShort?

    abstract suspend fun deleteMe()
}