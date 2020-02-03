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
    @Query(QUERY_CALENDAR)
    abstract fun observeCalendar(): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_CALENDAR_FILTER)
    abstract fun observeCalendarFilter(filter: String): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS)
    abstract fun observeAnimeWithStatus(status: RateStatus): Flow<List<AnimeWithRate>>

    @Transaction
    @Query(QUERY_ANIME_WITH_STATUS_FILTER)
    abstract fun observeAnimeWithStatus(status: RateStatus, filter: String): Flow<List<AnimeWithRate>>

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

        private const val QUERY_ANIME_WITH_STATUS_FILTER = """
            SELECT * from animes_fts AS fts
            JOIN animes AS a ON a.id = fts.docid
            INNER JOIN rates AS r ON r.anime_id = a.anime_shikimori_id
            WHERE r.status = :status
            AND animes_fts MATCH :filter
        """
    }
}
