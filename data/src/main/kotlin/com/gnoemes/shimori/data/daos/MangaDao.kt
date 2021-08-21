package com.gnoemes.shimori.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.manga.Manga
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MangaDao : EntityDao<Manga> {

    @Transaction
    @Query("SELECT * FROM mangas")
    abstract suspend fun queryAll(): List<Manga>

    @Transaction
    @Query(QUERY_BY_STATUS)
    abstract fun observeByStatus(status: RateStatus): Flow<List<MangaWithRate>>

    @Transaction
    @Query(QUERY_BY_STATUS_NAME_SORT)
    abstract fun pagingName(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_NAME_RU_SORT)
    abstract fun pagingNameRu(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_PROGRESS_SORT)
    abstract fun pagingProgress(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_CREATED_SORT)
    abstract fun pagingDateCreated(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_UPDATED_SORT)
    abstract fun pagingDateUpdated(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_AIRED_SORT)
    abstract fun pagingDateAired(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_SCORE_SORT)
    abstract fun pagingScore(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_SIZE_SORT)
    abstract fun pagingSize(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_RATING_SORT)
    abstract fun pagingRating(status: RateStatus, descending: Boolean): PagingSource<Int, MangaWithRate>

    @Transaction
    @Query(QUERY_RANDOM_PINNED)
    abstract suspend fun queryRandomPinned() : MangaWithRate?

    @Transaction
    @Query(QUERY_RANDOM_BY_STATUS)
    abstract suspend fun queryRandomByStatus(status: RateStatus) : MangaWithRate?

    companion object {
        private const val QUERY_BY_STATUS = """
            SELECT * FROM mangas as m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
        """

        private const val QUERY_BY_STATUS_NAME_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name END) DESC,
            (CASE :descending WHEN 0 THEN name END) ASC
        """

        private const val QUERY_BY_STATUS_NAME_RU_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name_ru_lower_case END) DESC,
            (CASE :descending WHEN 0 THEN name_ru_lower_case END) ASC
        """

        private const val QUERY_BY_STATUS_PROGRESS_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN chapters END) DESC,
            (CASE :descending WHEN 0 THEN chapters END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_CREATED_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_created) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_created) END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_UPDATED_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_updated) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_updated) END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_AIRED_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_aired) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_aired) END) ASC
        """

        private const val QUERY_BY_STATUS_SCORE_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN score END) DESC,
            (CASE :descending WHEN 0 THEN score END) ASC
        """

        private const val QUERY_BY_STATUS_SIZE_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN chapters_size END) DESC,
            (CASE :descending WHEN 0 THEN chapters_size END) ASC
        """

        private const val QUERY_BY_STATUS_RATING_SORT = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN rating END) DESC,
            (CASE :descending WHEN 0 THEN rating END) ASC
        """

        private const val QUERY_RANDOM_PINNED = """
            SELECT m.*, r.* FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            INNER JOIN pinned AS pin ON m.manga_shikimori_id = pin.target_id
            WHERE pin.target_type = "manga"
            ORDER BY RANDOM() LIMIT 1
        """

        private const val QUERY_RANDOM_BY_STATUS = """
            SELECT * FROM mangas AS m
            INNER JOIN rates AS r ON r.manga_id = m.manga_shikimori_id
            WHERE r.status = :status
            ORDER BY RANDOM() LIMIT 1
        """
    }
}