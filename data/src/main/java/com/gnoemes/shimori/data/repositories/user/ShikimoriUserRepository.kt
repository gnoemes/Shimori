package com.gnoemes.shimori.data.repositories.user

import com.gnoemes.shimori.data.base.database.daos.UserDao
import com.gnoemes.shimori.data.base.sources.UserDataSource
import com.gnoemes.shimori.data.base.utils.Shikimori

class ShikimoriUserRepository(
    private val dao: UserDao,
    @Shikimori private val source: UserDataSource
) {
    fun observeMeShort() = dao.observeMeShort()

    suspend fun queryMeShort() = dao.queryMeShort()

    suspend fun updateMe() {
        val user = source.getMyUser()
        dao.insertOrUpdate(user.copy(isMe = true))
    }

    suspend fun update(shikimoriId: Long) {
        val user = source.getUser(shikimoriId)
        val local = dao.queryMe()
        if (local != null && local.isSameShikimoriUser(user)) {
            dao.insertOrUpdate(user.copy(isMe = true))
            return
        }

        dao.insertOrUpdate(user)
    }

    suspend fun deleteMe() = dao.deleteMe()
}