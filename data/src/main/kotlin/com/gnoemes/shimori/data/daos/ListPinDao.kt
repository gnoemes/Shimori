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
    abstract fun queryAll(): List<ListPin>

    @Transaction
    @Query("SELECT * FROM pinned WHERE target_type = :type")
    abstract fun observeByType(type: RateTargetType): Flow<List<ListPin>>

    @Transaction
    @Query("SELECT COUNT(*) FROM pinned WHERE target_type = :type")
    abstract fun observePinnedSize(type: RateTargetType): Flow<Long>

}