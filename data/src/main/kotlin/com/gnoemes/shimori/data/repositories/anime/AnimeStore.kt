package com.gnoemes.shimori.data.repositories.anime

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

    //TODO: add all sorts from RateSortOption.kt
    fun observeAnimeWithStatus(status: RateStatus, sort: RateSort, filter: String?): Flow<List<AnimeWithRate>> =
        when (sort.sortOption) {
            RateSortOption.NAME -> observeNameSort(status, sort.isDescending, filter)
            RateSortOption.PROGRESS -> observeProgressSort(status, sort.isDescending, filter)
            RateSortOption.DATE_CREATED -> observeDateCreatedSort(status, sort.isDescending, filter)
            RateSortOption.DATE_UPDATED -> observeDateUpdatedSort(status, sort.isDescending, filter)
            RateSortOption.DATE_AIRED -> observeDateAiredSort(status, sort.isDescending, filter)
            RateSortOption.MY_SCORE -> observeScoreSort(status, sort.isDescending, filter)
            RateSortOption.SIZE -> observeSizeSort(status, sort.isDescending, filter)
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }

    private fun observeNameSort(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (settings.isRomadziNaming) observeNameSortRu(status, descending, filter)
        else if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByNameDesc(status)
            else animeDao.observeAnimeWithStatusByNameDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByName(status)
            else animeDao.observeAnimeWithStatusByName(status, "*$filter*")
        }
    }

    private fun observeNameSortRu(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByNameRuDesc(status)
            else animeDao.observeAnimeWithStatusByNameRuDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByNameRu(status)
            else animeDao.observeAnimeWithStatusByNameRu(status, "*$filter*")
        }
    }

    private fun observeProgressSort(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByProgressDesc(status)
            else animeDao.observeAnimeWithStatusByProgressDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByProgress(status)
            else animeDao.observeAnimeWithStatusByProgress(status, "*$filter*")
        }
    }

    private fun observeDateCreatedSort(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByDateCreatedDesc(status)
            else animeDao.observeAnimeWithStatusByDateCreatedDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByDateCreated(status)
            else animeDao.observeAnimeWithStatusByDateCreated(status, "*$filter*")
        }
    }

    private fun observeDateUpdatedSort(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByDateUpdatedDesc(status)
            else animeDao.observeAnimeWithStatusByDateUpdatedDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByDateUpdated(status)
            else animeDao.observeAnimeWithStatusByDateUpdated(status, "*$filter*")
        }
    }

    private fun observeDateAiredSort(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByDateAiredDesc(status)
            else animeDao.observeAnimeWithStatusByDateAiredDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByDateAired(status)
            else animeDao.observeAnimeWithStatusByDateAired(status, "*$filter*")
        }
    }

    private fun observeScoreSort(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByScoreDesc(status)
            else animeDao.observeAnimeWithStatusByScoreDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusByScore(status)
            else animeDao.observeAnimeWithStatusByScore(status, "*$filter*")
        }
    }

    private fun observeSizeSort(status: RateStatus, descending: Boolean, filter: String?): Flow<List<AnimeWithRate>> {
        return if (descending) {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusBySizeDesc(status)
            else animeDao.observeAnimeWithStatusBySizeDesc(status, "*$filter*")
        } else {
            if (filter.isNullOrBlank()) animeDao.observeAnimeWithStatusBySize(status)
            else animeDao.observeAnimeWithStatusBySize(status, "*$filter*")
        }
    }

}

