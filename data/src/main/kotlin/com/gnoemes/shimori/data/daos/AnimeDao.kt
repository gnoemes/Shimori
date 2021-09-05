package com.gnoemes.shimori.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AnimeDao : EntityDao<Anime> {

    @Transaction
    @Query("SELECT * FROM animes")
    abstract suspend fun queryAll(): List<Anime>

    @Transaction
    @Query(QUERY_ALL_WITH_STATUS)
    abstract suspend fun queryAllWithStatus(): List<AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS)
    abstract suspend fun queryByStatus(status: RateStatus): List<AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_ID)
    abstract fun observeById(id: Long) : Flow<AnimeWithRate?>

    @Transaction
    @Query(QUERY_CALENDAR)
    abstract fun observeCalendar(): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_CALENDAR_FILTER)
    abstract fun observeCalendarFilter(filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_BY_STATUS)
    abstract fun observeAnimeBYStatus(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_BY_STATUS_NAME_SORT)
    abstract fun pagingName(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_NAME_RU_SORT)
    abstract fun pagingNameRu(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_PROGRESS_SORT)
    abstract fun pagingProgress(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_CREATED_SORT)
    abstract fun pagingDateCreated(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_UPDATED_SORT)
    abstract fun pagingDateUpdated(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_DATE_AIRED_SORT)
    abstract fun pagingDateAired(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_SCORE_SORT)
    abstract fun pagingScore(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_SIZE_SORT)
    abstract fun pagingSize(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_BY_STATUS_RATING_SORT)
    abstract fun pagingRating(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_DATE_UPDATED_SORT)
    abstract fun pinnedDateUpdated(descending: Boolean) : Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_RANDOM_PINNED)
    abstract suspend fun queryRandomPinned(): AnimeWithRate?

    @Transaction
    @Query(QUERY_RANDOM_BY_STATUS)
    abstract suspend fun queryRandomByStatus(status: RateStatus): AnimeWithRate?


    companion object {
        private const val QUERY_BY_ID = """
           SELECT * FROM animes
           WHERE id = :id 
        """

        private const val QUERY_CALENDAR = """
           SELECT * FROM animes
           WHERE datetime(next_episode_date) > datetime('now','start of day') 
           ORDER BY datetime(next_episode_date) ASC
        """

        private const val QUERY_CALENDAR_FILTER = """
           SELECT * FROM animes_fts AS fts
           JOIN animes ON animes.id = fts.docid
           WHERE datetime(next_episode_date) > datetime('now','start of day')
           AND animes_fts MATCH :filter
           ORDER BY datetime(next_episode_date) ASC
        """

        //Null check just in case. Status actually can't be null if rate exist
        private const val QUERY_ALL_WITH_STATUS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status IS NOT NULL
        """

        private const val QUERY_BY_STATUS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
        """

        private const val QUERY_BY_STATUS_NAME_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name END) DESC,
            (CASE :descending WHEN 0 THEN name END) ASC
        """

        private const val QUERY_BY_STATUS_NAME_RU_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name_ru_lower_case END) DESC,
            (CASE :descending WHEN 0 THEN name_ru_lower_case END) ASC
        """

        private const val QUERY_BY_STATUS_PROGRESS_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN episodes END) DESC,
            (CASE :descending WHEN 0 THEN episodes END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_CREATED_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_created) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_created) END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_UPDATED_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_updated) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_updated) END) ASC
        """

        private const val QUERY_BY_STATUS_DATE_AIRED_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_aired) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_aired) END) ASC
        """

        private const val QUERY_BY_STATUS_SCORE_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN score END) DESC,
            (CASE :descending WHEN 0 THEN score END) ASC
        """

        private const val QUERY_BY_STATUS_SIZE_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN episodes_size END) DESC,
            (CASE :descending WHEN 0 THEN episodes_size END) ASC
        """

        private const val QUERY_BY_STATUS_RATING_SORT = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN rating END) DESC,
            (CASE :descending WHEN 0 THEN rating END) ASC
        """

        private const val QUERY_RANDOM_PINNED = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY RANDOM() LIMIT 1
        """

        private const val QUERY_RANDOM_BY_STATUS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY RANDOM() LIMIT 1
        """

        private const val QUERY_PINNED_DATE_UPDATED_SORT = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_updated) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_updated) END) ASC
        """
    }
}
