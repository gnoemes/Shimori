package com.gnoemes.shimori.data.user

import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.UserDao
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.source.track.TrackManager
import me.tatarka.inject.annotations.Inject

@Inject
class UserRepository(
    private val tracker: TrackManager,
    private val dao: UserDao,
    private val transactionRunner: DatabaseTransactionRunner,
) {
    fun observeMeShort(sourceId: Long) = dao.observeMeShort(sourceId)
    fun queryMeShort(sourceId: Long) = dao.queryMeShort(sourceId)

    suspend fun syncMe(sourceId: Long): SourceResponse<User> {
        return tracker.user(sourceId) { getMyUser() }.also {
            dao.update(it.data)
        }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            //todo add store
            val user = data.data
            if (user is User) {
                dao.upsert(user)
            }
        }
    }
}