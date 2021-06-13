package com.gnoemes.shimori.data.daos

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
    abstract fun observeAnimeWithStatusByName(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_FILTER)
    abstract fun observeAnimeWithStatusByName(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_DESC)
    abstract fun observeAnimeWithStatusByNameDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_DESC_FILTER)
    abstract fun observeAnimeWithStatusByNameDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU)
    abstract fun observeAnimeWithStatusByNameRu(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU_FILTER)
    abstract fun observeAnimeWithStatusByNameRu(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU_DESC)
    abstract fun observeAnimeWithStatusByNameRuDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_NAME_RU_DESC_FILTER)
    abstract fun observeAnimeWithStatusByNameRuDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS)
    abstract fun observeAnimeWithStatusByProgress(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS_FILTER)
    abstract fun observeAnimeWithStatusByProgress(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS_DESC)
    abstract fun observeAnimeWithStatusByProgressDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_PROGRESS_DESC_FILTER)
    abstract fun observeAnimeWithStatusByProgressDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED)
    abstract fun observeAnimeWithStatusByDateCreated(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_FILTER)
    abstract fun observeAnimeWithStatusByDateCreated(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_DESC)
    abstract fun observeAnimeWithStatusByDateCreatedDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_CREATED_DESC_FILTER)
    abstract fun observeAnimeWithStatusByDateCreatedDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED)
    abstract fun observeAnimeWithStatusByDateUpdated(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_FILTER)
    abstract fun observeAnimeWithStatusByDateUpdated(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_DESC)
    abstract fun observeAnimeWithStatusByDateUpdatedDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_UPDATED_DESC_FILTER)
    abstract fun observeAnimeWithStatusByDateUpdatedDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED)
    abstract fun observeAnimeWithStatusByDateAired(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_FILTER)
    abstract fun observeAnimeWithStatusByDateAired(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_DESC)
    abstract fun observeAnimeWithStatusByDateAiredDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_DATE_AIRED_DESC_FILTER)
    abstract fun observeAnimeWithStatusByDateAiredDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE)
    abstract fun observeAnimeWithStatusByScore(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE_FILTER)
    abstract fun observeAnimeWithStatusByScore(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE_DESC)
    abstract fun observeAnimeWithStatusByScoreDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SCORE_DESC_FILTER)
    abstract fun observeAnimeWithStatusByScoreDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE)
    abstract fun observeAnimeWithStatusBySize(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE_FILTER)
    abstract fun observeAnimeWithStatusBySize(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE_DESC)
    abstract fun observeAnimeWithStatusBySizeDesc(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_BY_SIZE_DESC_FILTER)
    abstract fun observeAnimeWithStatusBySizeDesc(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

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

    }
}
