package com.gnoemes.shimori.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
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
    @Query("SELECT * FROM rates WHERE manga_id = :id")
    abstract suspend fun queryByMangaId(id: Long): Rate?

    @Transaction
    @Query("SELECT * FROM rates WHERE manga_id IN (:ids)")
    abstract suspend fun queryByMangaIds(ids: List<Long>): List<Rate>

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

    @Transaction
    @Query(QUERY_PAGE_EXIST)
    abstract fun observePageExist(target: RateTargetType, status: RateStatus): Flow<Int>

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

        private const val QUERY_PAGE_EXIST = """
            SELECT COUNT(*) FROM rates 
            WHERE target_type = :target
            AND status = :status 
            LIMIT 1
        """
    }

}