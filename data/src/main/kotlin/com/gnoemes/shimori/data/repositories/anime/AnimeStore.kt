package com.gnoemes.shimori.data.repositories.anime

import androidx.paging.PagingSource
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.data.daos.AnimeDao
import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.daos.ListPinDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeStore @Inject constructor(
    private val inserter: EntityInserter,
    private val runner: DatabaseTransactionRunner,
    private val animeDao: AnimeDao,
    private val settings: ShimoriPreferences,
    private val pinDao: ListPinDao,
) {

    private val syncer = syncerForEntity(
            animeDao,
            { it.shikimoriId },
            { entity, id -> entity.copy(id = id ?: 0) }
    )

    suspend fun updateAnimes(animes: List<Anime>) = runner {
        syncer.sync(animeDao.queryAll(), animes, removeNotMatched = false)
    }

    suspend fun queryAnimesWithStatus(status: RateStatus) = animeDao.queryAnimesWithStatus(status)

    fun observeCalendar(filter: String?): Flow<List<AnimeWithRate>> = when {
        filter.isNullOrBlank() -> animeDao.observeCalendar()
        else -> animeDao.observeCalendarFilter("*$filter*")
    }

    fun observeByStatusForPaging(
        status: RateStatus?,
        sort: RateSort
    ): PagingSource<Int, AnimeWithRate> {
        if (status == null) return observePinnedForPaging(sort)

        return when (sort.sortOption) {
            RateSortOption.NAME -> {
                //TODO paging eng
                if (!settings.isRomadziNaming) animeDao.pagingNameRu(status, sort.isDescending)
                else animeDao.pagingName(status, sort.isDescending)
            }
            RateSortOption.PROGRESS -> animeDao.pagingProgress(status, sort.isDescending)
            RateSortOption.DATE_CREATED -> animeDao.pagingDateCreated(status, sort.isDescending)
            RateSortOption.DATE_UPDATED -> animeDao.pagingdDateUpdated(status, sort.isDescending)
            RateSortOption.DATE_AIRED -> animeDao.pagingDateAired(status, sort.isDescending)
            RateSortOption.MY_SCORE -> animeDao.pagingScore(status, sort.isDescending)
            RateSortOption.SIZE -> animeDao.pagingSize(status, sort.isDescending)
            RateSortOption.RATING -> animeDao.pagingRating(status, sort.isDescending)
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }

    private fun observePinnedForPaging(sort: RateSort): PagingSource<Int, AnimeWithRate> {
        return when (sort.sortOption) {
            RateSortOption.NAME -> {
                //TODO paging eng
                if (!settings.isRomadziNaming) animeDao.pagingPinnedNameRu(sort.isDescending)
                else animeDao.pagingPinnedName(sort.isDescending)
            }
            RateSortOption.PROGRESS -> animeDao.pagingPinnedProgress(sort.isDescending)
            RateSortOption.DATE_CREATED -> animeDao.pagingPinnedDateCreated(sort.isDescending)
            RateSortOption.DATE_UPDATED -> animeDao.pagingPinnedDateUpdated(sort.isDescending)
            RateSortOption.DATE_AIRED -> animeDao.pagingPinnedDateAired(sort.isDescending)
            RateSortOption.MY_SCORE -> animeDao.pagingPinnedScore(sort.isDescending)
            RateSortOption.SIZE -> animeDao.pagingPinnedSize(sort.isDescending)
            RateSortOption.RATING -> animeDao.pagingPinnedRating(sort.isDescending)
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }
}

