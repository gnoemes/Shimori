package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.user.User
import com.gnoemes.shimori.data.core.entities.user.UserShort
import kotlinx.coroutines.flow.Flow

abstract class UserDao : EntityDao<User>() {
    abstract fun observeMeShort(sourceId : Long): Flow<UserShort?>

    abstract suspend fun queryMe(sourceId : Long): User?
    abstract suspend fun queryMeShort(sourceId : Long): UserShort?

    abstract suspend fun deleteMe(sourceId : Long)
}