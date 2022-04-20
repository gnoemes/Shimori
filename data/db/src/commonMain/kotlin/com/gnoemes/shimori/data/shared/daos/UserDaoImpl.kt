package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.database.daos.UserDao
import com.gnoemes.shimori.data.base.entities.user.User
import com.gnoemes.shimori.data.base.entities.user.UserShort
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.UserMapper
import com.gnoemes.shimori.data.shared.queryMeShortToUserShortMapper
import com.gnoemes.shimori.data.shared.singleResult
import com.gnoemes.shimori.data.shared.userToUserShortMapper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow

internal class UserDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : UserDao() {

    override suspend fun insert(entity: User) {
        UserMapper.mapInverse(entity)?.let { db.userQueries.insert(it) }
    }

    override suspend fun deleteEntity(entity: User) {
        db.userQueries.deleteById(entity.id)
    }

    override suspend fun deleteMe() {
        db.userQueries.deleteMe()
    }

    override fun observeMeShort(): Flow<UserShort?> {
        return db.userQueries.queryMe()
            .asFlow()
            .singleResult(userToUserShortMapper::map)
    }

    override suspend fun queryMe(): User? {
        return db.userQueries.queryMe().executeAsOneOrNull()?.let { UserMapper.map(it) }
    }

    override suspend fun queryMeShort(): UserShort? {
        return db.userQueries.queryMeShort().executeAsOneOrNull()?.let {
            queryMeShortToUserShortMapper.map(it)
        }
    }
}