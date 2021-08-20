package com.gnoemes.shimori.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.app.ListPin
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ListPinDao : EntityDao<ListPin> {

    @Transaction
    @Query("SELECT * from pinned")
    abstract suspend fun queryAll(): List<ListPin>

    @Transaction
    @Query("SELECT * FROM pinned WHERE target_id = :id")
    abstract suspend fun queryById(id: Long): ListPin?

    @Transaction
    @Query("SELECT COUNT(*) FROM pinned")
    abstract fun observeSize() : Flow<Int>

    @Transaction
    @Query("SELECT * FROM pinned WHERE target_type = :type")
    abstract fun observeByType(type: RateTargetType): Flow<List<ListPin>>

}