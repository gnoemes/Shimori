package com.gnoemes.shimori.data.user

import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.UserDao
import com.gnoemes.shimori.data.source.SourceIds
import com.gnoemes.shimori.data.source.track.TrackManager
import me.tatarka.inject.annotations.Inject

@Inject
class UserRepository(
    private val tracker: TrackManager,
    private val dao: UserDao
) {
    fun observeMeShort(sourceId: Long) = dao.observeMeShort(sourceId)
    fun queryMeShort(sourceId: Long) = dao.queryMeShort(sourceId)

    suspend fun syncMe(): SourceResponse<User> {
        //TODO sync from all sources
        return tracker.user(SourceIds.SHIKIMORI) { getMyUser() }.also {
            dao.update(it.data)
        }
    }
}