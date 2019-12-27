package com.gnoemes.shimori.data.daos

import androidx.room.*
import com.gnoemes.shimori.model.app.LastRequest
import com.gnoemes.shimori.model.app.Request

@Dao
abstract class LastRequestDao : EntityDao<LastRequest> {
    @Query("SELECT * FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract suspend fun lastRequest(request: Request, entityId: Long): LastRequest?

    @Query("SELECT COUNT(*) FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract suspend fun requestCount(request: Request, entityId: Long): Int

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: LastRequest): Long
}