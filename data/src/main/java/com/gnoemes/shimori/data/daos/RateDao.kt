package com.gnoemes.shimori.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.rate.Rate
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RateDao : EntityDao<Rate> {

    @Transaction
    @Query("SELECT * FROM rates")
    abstract suspend fun queryAll(): List<Rate>

    @Transaction
    @Query("SELECT * FROM rates WHERE id = :id")
    abstract suspend fun queryWithId(id: Long): Rate?

    @Transaction
    @Query("SELECT * FROM rates WHERE shikimori_id IN (:ids)")
    abstract suspend fun queryWithShikimoriIds(ids: List<Long>): List<Rate>

    @Transaction
    @Query(QUERY_BY_ANIME_TARGET)
    abstract suspend fun queryAnimeRate(id: Long): Rate?

    @Transaction
    @Query("DELETE FROM rates")
    abstract suspend fun deleteAll()

    @Transaction
    @Query("DELETE FROM rates WHERE id = :id")
    abstract suspend fun deleteWithId(id: Long): Int

    @Transaction
    @Query("DELETE FROM rates WHERE id IN (:ids)")
    abstract suspend fun deleteWithIds(ids: List<Long>): Int

    @Transaction
    @Query(QUERY_ANIME_RATES)
    abstract fun observeAnimeRates(): Flow<List<Rate>>

    @Transaction
    @Query("SELECT * from rates WHERE shikimori_id = :shikimoriId")
    abstract fun observeRate(shikimoriId: Long): Flow<Rate?>

    companion object {
        private const val QUERY_BY_ANIME_TARGET = """
           SELECT * from rates
           WHERE target_type = "anime"
           AND anime_id = :id
        """

        private const val QUERY_ANIME_RATES = """
           SELECT * FROM rates AS r
           WHERE r.target_type = "anime"
        """

        private const val QUERY_ANIME_RATES_FILTER = """
           SELECT * FROM rates AS r
           INNER JOIN animes AS a ON r.target_id = a.shikimori_id
           INNER JOIN animes_fts AS fts ON a.id = fts.docid
           WHERE r.target_type = "anime"
        """

        private const val QUERY_ANIME_RATES_WITH_STATUS = """
            $QUERY_ANIME_RATES
            AND r.status = :status
        """

        private const val QUERY_ANIME_RATES_WITH_STATUS_FILTER = """
            $QUERY_ANIME_RATES_FILTER
            AND r.status = :status
            AND animes_fts MATCH :filter
        """

        private const val QUERY_ANIME_RATES_WITH_STATUS_NAME = """
            $QUERY_ANIME_RATES_WITH_STATUS
            ORDER BY a.name DESC
        """

        private const val QUERY_ANIME_RATES_WITH_STATUS_NAME_FILTER = """
            $QUERY_ANIME_RATES_WITH_STATUS_FILTER
            ORDER BY a.name DESC
        """
    }

}