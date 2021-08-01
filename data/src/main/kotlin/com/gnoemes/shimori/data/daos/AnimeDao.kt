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
    abstract fun pagingName(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_FILTER)
    abstract fun pagingName(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_DESC)
    abstract fun pagingNameDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_DESC_FILTER)
    abstract fun pagingNameDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU)
    abstract fun pagingNameRu(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU_FILTER)
    abstract fun pagingNameRu(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU_DESC)
    abstract fun pagingNameRuDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU_DESC_FILTER)
    abstract fun pagingNameRuDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS)
    abstract fun pagingProgress(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS_FILTER)
    abstract fun pagingProgress(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS_DESC)
    abstract fun pagingProgressDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS_DESC_FILTER)
    abstract fun pagingProgressDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED)
    abstract fun pagingDateCreated(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_FILTER)
    abstract fun pagingDateCreated(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_DESC)
    abstract fun pagingDateCreatedDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_DESC_FILTER)
    abstract fun pagingDateCreatedDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED)
    abstract fun pagedDateUpdated(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_FILTER)
    abstract fun pagedDateUpdated(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_DESC)
    abstract fun pagingDateUpdatedDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_DESC_FILTER)
    abstract fun pagingDateUpdatedDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED)
    abstract fun pagedDateAired(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_FILTER)
    abstract fun pagedDateAired(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_DESC)
    abstract fun pagedDateAiredDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_DESC_FILTER)
    abstract fun pagedDateAiredDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE)
    abstract fun pagedScore(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE_FILTER)
    abstract fun pagedScore(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE_DESC)
    abstract fun pagedScoreDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE_DESC_FILTER)
    abstract fun pagedScoreDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE)
    abstract fun pagedSize(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE_FILTER)
    abstract fun pagedSize(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE_DESC)
    abstract fun pagedSizeDesc(status: RateStatus): PagingSource<Int, AnimeWithRate>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE_DESC_FILTER)
    abstract fun pagedSizeDesc(status: RateStatus, filter: String): PagingSource<Int, AnimeWithRate>

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

        private const val QUERY_ANIME_WITH_STATUS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY name ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status 
            AND animes_fts MATCH :filter
            ORDER BY name ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY name DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status 
            AND animes_fts MATCH :filter
            ORDER BY name DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_RU = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY name_ru_lower_case ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_RU_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status 
            AND animes_fts MATCH :filter
            ORDER BY name_ru_lower_case ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_RU_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY name_ru_lower_case DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_NAME_RU_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status 
            AND animes_fts MATCH :filter
            ORDER BY name_ru_lower_case DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_PROGRESS = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY episodes ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_PROGRESS_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter
            ORDER BY episodes ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_PROGRESS_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY episodes DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_PROGRESS_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter
            ORDER BY episodes DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY datetime(date_created) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter 
            ORDER BY datetime(date_created) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY datetime(date_created) DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter 
            ORDER BY datetime(date_created) DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY datetime(date_updated) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter 
            ORDER BY datetime(date_updated) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY datetime(date_updated) DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter 
            ORDER BY datetime(date_updated) DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY datetime(date_aired) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter 
            ORDER BY datetime(date_aired) ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY datetime(date_aired) DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter 
            ORDER BY datetime(date_aired) DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SCORE = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY score ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SCORE_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter
            ORDER BY score ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SCORE_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY score DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SCORE_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter
            ORDER BY score DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SIZE = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY episodes_size ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SIZE_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter
            ORDER BY episodes_size ASC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SIZE_DESC = """
            SELECT * from animes AS a
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            ORDER BY episodes_size DESC
        """

        private const val QUERY_ANIME_WITH_STATUS_BY_SIZE_DESC_FILTER = """
            SELECT * FROM animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter
            ORDER BY episodes_size DESC
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
    }
}
