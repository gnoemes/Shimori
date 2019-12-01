package com.gnoemes.shimori.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.CalendarItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AnimeDao : EntityDao<Anime> {

    @Transaction
    @Query(QUERY_CALENDAR)
    abstract fun observeCalendar(): Flow<List<CalendarItem>>

    @Transaction
    @Query(QUERY_CALENDAR_FILTER)
    abstract fun observeCalendarFilter(filter: String): Flow<List<CalendarItem>>

    companion object {
        private const val QUERY_CALENDAR = """
           SELECT * FROM animes
           LEFT JOIN rates ON animes.rate_id = rates.shikimori_id
           WHERE datetime(next_episode_date) < datetime('now','start of day','+1 day','-1 minute') 
           ORDER BY datetime(next_episode_date) ASC
        """
        private const val QUERY_CALENDAR_FILTER = """
           SELECT * FROM animes
           LEFT JOIN rates ON animes.rate_id = rates.shikimori_id
           WHERE datetime(next_episode_date) < datetime('now','start of day','+1 day','-1 minute') 
           AND name MATCH :filter
           OR name_ru MATCH :filter
           ORDER BY datetime(next_episode_date) ASC
        """
    }
}