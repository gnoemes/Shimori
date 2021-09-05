package com.gnoemes.shimori.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.ranobe.Ranobe
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RanobeDao : EntityDao<Ranobe> {

    @Transaction
    @Query("SELECT * FROM ranobe")
    abstract suspend fun queryAll(): List<Ranobe>

    @Transaction
    @Query(QUERY_BY_ID)
    abstract fun observeById(id: Long): Flow<RanobeWithRate?>

    @Transaction
    @Query(QUERY_BY_STATUS)
    abstract fun observeByStatus(status: RateStatus): Flow<List<RanobeWithRate>>

    @Transaction
    @Query(QUERY_BY_STATUS_NAME_SORT)
    abstract fun pagingName(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_NAME_RU_SORT)
    abstract fun pagingNameRu(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_PROGRESS_SORT)
    abstract fun pagingProgress(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_CREATED_SORT)
    abstract fun pagingDateCreated(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_UPDATED_SORT)
    abstract fun pagingDateUpdated(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_AIRED_SORT)
    abstract fun pagingDateAired(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_SCORE_SORT)
    abstract fun pagingScore(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_SIZE_SORT)
    abstract fun pagingSize(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_RATING_SORT)
    abstract fun pagingRating(status: RateStatus, descending: Boolean): PagingSource<Int, RanobeWithRate>

    @Transaction
    @Query(QUERY_PINNED_DATE_UPDATED_SORT)
    abstract fun pinnedDateUpdated(descending: Boolean): Flow<List<RanobeWithRate>>

    @Transaction
    @Query(QUERY_RANDOM_PINNED)
    abstract suspend fun queryRandomPinned(): RanobeWithRate?

    @Transaction
    @Query(QUERY_RANDOM_BY_STATUS)
    abstract suspend fun queryRandomByStatus(status: RateStatus): RanobeWithRate?

    companion object {
        private const val QUERY_BY_ID = """
            SELECT * FROM ranobe
            WHERE id = :id
        """

        private const val QUERY_BY_STATUS = """
            SELECT * FROM ranobe as t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
        """

        private const val QUERY_BY_STATUS_NAME_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name END) DESC,
            (CASE :descending WHEN 0 THEN name END) ASC
        """

        private const val QUERY_BY_STATUS_NAME_RU_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name_ru_lower_case END) DESC,
            (CASE :descending WHEN 0 THEN name_ru_lower_case END) ASC
        """

        private const val QUERY_BY_STATUS_PROGRESS_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN chapters END) DESC,
            (CASE :descending WHEN 0 THEN chapters END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_CREATED_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_created) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_created) END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_UPDATED_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_updated) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_updated) END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_AIRED_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_aired) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_aired) END) ASC
        """

        private const val QUERY_BY_STATUS_SCORE_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN score END) DESC,
            (CASE :descending WHEN 0 THEN score END) ASC
        """

        private const val QUERY_BY_STATUS_SIZE_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN chapters_size END) DESC,
            (CASE :descending WHEN 0 THEN chapters_size END) ASC
        """

        private const val QUERY_BY_STATUS_RATING_SORT = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN rating END) DESC,
            (CASE :descending WHEN 0 THEN rating END) ASC
        """

        private const val QUERY_RANDOM_PINNED = """
            SELECT t.*, r.* FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            INNER JOIN pinned AS pin ON t.id = pin.target_id
            WHERE pin.target_type = "ranobe"
            ORDER BY RANDOM() LIMIT 1
        """

        private const val QUERY_RANDOM_BY_STATUS = """
            SELECT * FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            WHERE r.status = :status
            ORDER BY RANDOM() LIMIT 1
        """

        private const val QUERY_PINNED_DATE_UPDATED_SORT = """
            SELECT t.* FROM ranobe AS t
            INNER JOIN rates AS r ON r.ranobe_id = t.ranobe_shikimori_id
            INNER JOIN pinned AS pin ON t.id = pin.target_id
            WHERE pin.target_type = "ranobe"
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_updated) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_updated) END) ASC
        """
    }
}