package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.data.user.UserShort
import kotlinx.coroutines.flow.Flow

interface UserDao : EntityDao<User> {
    fun observeMeShort(sourceId: Long): Flow<UserShort?>

    fun queryMe(sourceId: Long): User?
    fun queryMeShort(sourceId: Long): UserShort?

    fun deleteMe(sourceId: Long)
}