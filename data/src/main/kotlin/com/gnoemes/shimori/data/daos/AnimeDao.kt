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
    @Query(QUERY_ALL_ANIME_WITH_STATUS)
    abstract suspend fun queryAllAnimesWithStatus() : List<AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS)
    abstract suspend fun queryAnimesWithStatus(status: RateStatus): List<AnimeWithRate>

    @Transaction
    @Query(QUERY_CALENDAR)
    abstract fun observeCalendar(): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_CALENDAR_FILTER)
    abstract fun observeCalendarFilter(filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS)
    abstract fun observeAnimeWithStatus(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME)
    abstract fun pagingName(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU)
    abstract fun pagingNameRu(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS)
    abstract fun pagingProgress(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED)
    abstract fun pagingDateCreated(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED)
    abstract fun pagingdDateUpdated(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED)
    abstract fun pagingDateAired(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE)
    abstract fun pagingScore(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE)
    abstract fun pagingSize(status: RateStatus, descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_WITH_STATUS_BY_RATING)
    abstract fun pagingRating(status: RateStatus, descending: Boolean) : PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_NAME)
    abstract fun pagingPinnedName(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_NAME_RU)
    abstract fun pagingPinnedNameRu(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_PROGRESS)
    abstract fun pagingPinnedProgress(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_DATE_CREATED)
    abstract fun pagingPinnedDateCreated(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_DATE_UPDATED)
    abstract fun pagingPinnedDateUpdated(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_DATE_AIRED)
    abstract fun pagingPinnedDateAired(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_SCORE)
    abstract fun pagingPinnedScore(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_SIZE)
    abstract fun pagingPinnedSize(descending: Boolean): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_PINNED_BY_RATING)
    abstract fun pagingPinnedRating(descending: Boolean): PagingSource<Int, AnimeWithRate>

    companion object {
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
        private const val QUERY_ALL_ANIME_WITH_STATUS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status IS NOT NULL
        """

        private const val QUERY_ANIME_WITH_STATUS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name END) DESC,
            (CASE :descending WHEN 0 THEN name END) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_RU = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN name_ru_lower_case END) DESC,
            (CASE :descending WHEN 0 THEN name_ru_lower_case END) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_PROGRESS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN episodes END) DESC,
            (CASE :descending WHEN 0 THEN episodes END) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_created) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_created) END) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_updated) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_updated) END) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_aired) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_aired) END) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SCORE = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN score END) DESC,
            (CASE :descending WHEN 0 THEN score END) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SIZE = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN episodes_size END) DESC,
            (CASE :descending WHEN 0 THEN episodes_size END) ASC
        """

        private const val QUERY_WITH_STATUS_BY_RATING = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY  
            (CASE :descending WHEN 1 THEN rating END) DESC,
            (CASE :descending WHEN 0 THEN rating END) ASC
        """

        private const val QUERY_PINNED_BY_NAME = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN name END) DESC,
            (CASE :descending WHEN 0 THEN name END) ASC
        """

        private const val QUERY_PINNED_BY_NAME_RU = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN name_ru_lower_case END) DESC,
            (CASE :descending WHEN 0 THEN name_ru_lower_case END) ASC
        """

        private const val QUERY_PINNED_BY_PROGRESS = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN episodes END) DESC,
            (CASE :descending WHEN 0 THEN episodes END) ASC
        """

        private const val QUERY_PINNED_BY_DATE_CREATED = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_created) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_created) END) ASC
        """

        private const val QUERY_PINNED_BY_DATE_UPDATED = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_updated) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_updated) END) ASC
        """

        private const val QUERY_PINNED_BY_DATE_AIRED = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN datetime(date_aired) END) DESC,
            (CASE :descending WHEN 0 THEN datetime(date_aired) END) ASC
        """

        private const val QUERY_PINNED_BY_SCORE = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN score END) DESC,
            (CASE :descending WHEN 0 THEN score END) ASC
        """

        private const val QUERY_PINNED_BY_SIZE = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN episodes_size END) DESC,
            (CASE :descending WHEN 0 THEN episodes_size END) ASC
        """

        private const val QUERY_PINNED_BY_RATING = """
            SELECT a.*, r.* FROM animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            INNER JOIN pinned AS pin ON a.anime_shikimori_id = pin.target_id
            WHERE pin.target_type = "anime"
            ORDER BY  
            (CASE :descending WHEN 1 THEN rating END) DESC,
            (CASE :descending WHEN 0 THEN rating END) ASC
        """

    }
}
