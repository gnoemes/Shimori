package com.gnoemes.shimori.data.repositories.user

import com.gnoemes.shimori.data.core.database.daos.UserDao
import com.gnoemes.shimori.data.core.entities.user.User

class UserRepository(
    private val dao: UserDao,
) {
    fun observeMeShort(sourceId: Long) = dao.observeMeShort(sourceId)

    suspend fun queryMeShort(sourceId: Long) = dao.queryMeShort(sourceId)

    suspend fun syncMe(remote: User) {
        dao.insertOrUpdate(
            remote.copy(isMe = true)
        )
    }

    suspend fun sync(remote: User) {
        val local = dao.queryMe(remote.sourceId)
        dao.insertOrUpdate(
            remote.copy(
                isMe = local != null && local.remoteId == remote.remoteId
            )
        )
    }

    suspend fun deleteMe(sourceId: Long) = dao.deleteMe(sourceId)
}