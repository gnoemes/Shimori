package com.gnoemes.shimori.data.daos

import androidx.room.*
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RateSortDao : EntityDao<RateSort> {

    @Query(QUERY_SORT)
    abstract suspend fun querySort(type: RateTargetType): RateSort?

    @Transaction
    @Query(QUERY_SORT)
    abstract fun observeSort(type: RateTargetType): Flow<RateSort?>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: RateSort): Long

    companion object {
        private const val QUERY_SORT = """
            SELECT * FROM rate_sort WHERE type = :type
        """
    }

}