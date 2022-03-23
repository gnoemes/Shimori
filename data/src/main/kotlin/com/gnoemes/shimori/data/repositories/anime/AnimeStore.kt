package com.gnoemes.shimori.data.repositories.anime

import androidx.paging.PagingSource
import com.gnoemes.shimori.data.daos.AnimeDao
import com.gnoemes.shimori.data.daos.EntityInserter
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
) {

    private val syncer = syncerForEntity(
        animeDao,
        { it.shikimoriId },
        { remote, local -> remote.copy(id = local?.id ?: 0) }
    )

    suspend fun update(animes: List<Anime>) = runner {
        syncer.sync(animeDao.queryAll(), animes, removeNotMatched = false)
    }

    suspend fun queryByStatus(status: RateStatus?) =
        if (status == null) animeDao.queryAllWithStatus()
        else animeDao.queryByStatus(status)

    suspend fun queryRandomByStatus(status: RateStatus?) =
        if (status == null) animeDao.queryRandomPinned()
        else animeDao.queryRandomByStatus(status)

    fun observeCalendar(filter: String?): Flow<List<AnimeWithRate>> = when {
        filter.isNullOrBlank() -> animeDao.observeCalendar()
        else -> animeDao.observeCalendarFilter("*$filter*")
    }

    fun observeByStatusForPaging(
        status: RateStatus,
        sort: RateSort
    ): PagingSource<Int, AnimeWithRate> {
        return when (sort.sortOption) {
            RateSortOption.NAME -> {
                //TODO paging eng, restore romadzi or make cross results
//                if (!settings.isRomadziNaming) animeDao.pagingNameRu(status, sort.isDescending)
//                else
                animeDao.pagingName(status, sort.isDescending)
            }
            RateSortOption.PROGRESS -> animeDao.pagingProgress(status, sort.isDescending)
            RateSortOption.DATE_CREATED -> animeDao.pagingDateCreated(status, sort.isDescending)
            RateSortOption.DATE_UPDATED -> animeDao.pagingDateUpdated(status, sort.isDescending)
            RateSortOption.DATE_AIRED -> animeDao.pagingDateAired(status, sort.isDescending)
            RateSortOption.MY_SCORE -> animeDao.pagingScore(status, sort.isDescending)
            RateSortOption.SIZE -> animeDao.pagingSize(status, sort.isDescending)
            RateSortOption.RATING -> animeDao.pagingRating(status, sort.isDescending)
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }

    fun observePinned(sort: RateSort): Flow<List<AnimeWithRate>> {
        return when (sort.sortOption) {
            RateSortOption.PROGRESS -> animeDao.pinnedProgress(sort.isDescending)
            RateSortOption.DATE_CREATED -> animeDao.pinnedDateCreated(sort.isDescending)
            RateSortOption.DATE_UPDATED -> animeDao.pinnedDateUpdated(sort.isDescending)
            RateSortOption.DATE_AIRED -> animeDao.pinnedDateAired(sort.isDescending)
            RateSortOption.MY_SCORE -> animeDao.pinnedScore(sort.isDescending)
            RateSortOption.RATING -> animeDao.pinnedRating(sort.isDescending)
            RateSortOption.NAME -> {
                //TODO paging eng, restore romadzi or make cross results
//                if (!settings.isRomadziNaming) animeDao.pagingNameRu(status, sort.isDescending)
//                else
                animeDao.pinnedName(sort.isDescending)
            }
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }

    fun observeById(id: Long) = animeDao.observeById(id)
}

