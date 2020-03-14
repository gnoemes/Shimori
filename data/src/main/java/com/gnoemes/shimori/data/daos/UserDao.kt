package com.gnoemes.shimori.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.user.User
import com.gnoemes.shimori.model.user.UserShort
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : EntityDao<User> {

    @Transaction
    @Query(QUERY_ME)
    abstract fun observeMe(): Flow<User?>

    @Transaction
    @Query(QUERY_ME_SHORT)
    abstract fun observeMeShort(): Flow<UserShort?>

    @Transaction
    @Query(QUERY_ME)
    abstract suspend fun queryMe(): User?

    @Transaction
    @Query(QUERY_MY_ID)
    abstract suspend fun queryMyId(): Long?

    @Transaction
    @Query(QUERY_USER_BY_ID)
    abstract suspend fun queryUser(id: Long): User?

    companion object {
        private const val QUERY_ME = """
           SELECT * FROM users
           WHERE is_me = 1
        """

        private const val QUERY_ME_SHORT = """
           SELECT id, shikimori_id, nickname, image_original, image_preview, image_x96, image_x48 FROM users
           WHERE is_me = 1
        """

        private const val QUERY_MY_ID = """
           SELECT shikimori_id FROM users
           WHERE is_me = 1
        """

        private const val QUERY_USER_BY_ID = """
           SELECT * FROM users
           WHERE shikimori_id = :id
        """
    }
}