package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.data.user.UserShort
import kotlinx.coroutines.flow.Flow

interface UserDao : EntityDao<User> {
    fun observeMeShort(sourceId: Long): Flow<UserShort?>

    suspend fun queryMe(sourceId: Long): User?
    suspend fun queryMeShort(sourceId: Long): UserShort?

    suspend fun deleteMe(sourceId: Long)
}