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
        val filter = null

        if (status == null) return observePinnedForPaging(sort)

        return when (sort.sortOption) {
            RateSortOption.NAME -> pagingName(status, sort.isDescending, filter)
            RateSortOption.PROGRESS -> observeProgressSort(status, sort.isDescending, filter)
            RateSortOption.DATE_CREATED -> observeDateCreatedSort(status, sort.isDescending, filter)
            RateSortOption.DATE_UPDATED -> observeDateUpdatedSort(status, sort.isDescending, filter)
            RateSortOption.DATE_AIRED -> observeDateAiredSort(status, sort.isDescending, filter)
            RateSortOption.MY_SCORE -> observeScoreSort(status, sort.isDescending, filter)
            RateSortOption.SIZE -> observeSizeSort(status, sort.isDescending, filter)
            RateSortOption.RATING -> animeDao.pagedRating(status, sort.isDescending)
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }

    private fun observePinnedForPaging(sort: RateSort): PagingSource<Int, AnimeWithRate> {
        return when (sort.sortOption) {
            RateSortOption.NAME -> {
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

    //TODO paging eng
    private fun pagingName(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (!settings.isRomadziNaming) pagingNameRu(status, descending, filter)
        else if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagingNameDesc(status)
            else animeDao.pagingNameDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagingName(status)
            else animeDao.pagingName(status, "*$filter*")
        }
    }

    private fun pagingNameRu(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagingNameRuDesc(status)
            else animeDao.pagingNameRuDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagingNameRu(status)
            else animeDao.pagingNameRu(status, "*$filter*")
        }
    }

    private fun observeProgressSort(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagingProgressDesc(status)
            else animeDao.pagingProgressDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagingProgress(status)
            else animeDao.pagingProgress(status, "*$filter*")
        }
    }

    private fun observeDateCreatedSort(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagingDateCreatedDesc(status)
            else animeDao.pagingDateCreatedDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagingDateCreated(status)
            else animeDao.pagingDateCreated(status, "*$filter*")
        }
    }

    private fun observeDateUpdatedSort(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagingDateUpdatedDesc(status)
            else animeDao.pagingDateUpdatedDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagedDateUpdated(status)
            else animeDao.pagedDateUpdated(status, "*$filter*")
        }
    }

    private fun observeDateAiredSort(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagedDateAiredDesc(status)
            else animeDao.pagedDateAiredDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagedDateAired(status)
            else animeDao.pagedDateAired(status, "*$filter*")
        }
    }

    private fun observeScoreSort(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagedScoreDesc(status)
            else animeDao.pagedScoreDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagedScore(status)
            else animeDao.pagedScore(status, "*$filter*")
        }
    }

    private fun observeSizeSort(status: RateStatus, descending: Boolean, filter: String?): PagingSource<Int, AnimeWithRate> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.pagedSizeDesc(status)
            else animeDao.pagedSizeDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.pagedSize(status)
            else animeDao.pagedSize(status, "*$filter*")
        }
    }
}

