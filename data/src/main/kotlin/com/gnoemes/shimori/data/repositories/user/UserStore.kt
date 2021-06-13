package com.gnoemes.shimori.data.repositories.user

import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.daos.UserDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.user.User
import com.gnoemes.shimori.model.user.UserShort
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserStore @Inject constructor(
    private val inserter: EntityInserter,
    private val runner: DatabaseTransactionRunner,
    private val dao: UserDao
) {

    private val syncer = syncerForEntity(
            dao,
            { it.shikimoriId },
            { entity, id -> entity.copy(id = id ?: 0) }
    )

    fun observeMeShort() : Flow<UserShort?> {
        return dao.observeMeShort()
    }

    fun observeMe(): Flow<User?> {
        return dao.observeMe()
    }

    suspend fun deleteMe() = dao.deleteMe()

    suspend fun queryMe(): User? = dao.queryMe()

    suspend fun queryMyId(): Long? = dao.queryMyId()

    suspend fun save(user: User) = runner {
        val currentValues = user.shikimoriId?.let {
            dao.queryUser(it)?.let { user ->
                listOf(user)
            }
        } ?: emptyList()

        val remoteValues = listOf(user)

        syncer.sync(currentValues, remoteValues, false)
    }
}