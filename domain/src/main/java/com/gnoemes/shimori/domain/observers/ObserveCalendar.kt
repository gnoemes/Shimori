package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.calendar.CalendarRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.anime.AnimeWithRate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.joda.time.DateTime
import javax.inject.Inject

class ObserveCalendar @Inject constructor(
    private val repository: CalendarRepository,
    dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<ObserveCalendar.Params, Calendar>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<Calendar> {
        return repository.observeCalendar(params.filter?.toLowerCase())
            .mapLatest { groupByDates(it) }
    }

    //TODO split, hard to understand
    private fun groupByDates(animes: List<AnimeWithRate>): Calendar {
        val groupList = mutableListOf<Pair<DateTime, List<AnimeWithRate>>>()

        if (animes.isEmpty()) return groupList

        var groupDate = animes.firstOrNull()?.entity?.nextEpisodeDate
        val groupAnimes = mutableListOf<AnimeWithRate>()

        var todayPastFlag = groupDate?.let { !DateTime.now().isBefore(it) } ?: true

        fun addGroup() {
            groupList.add(groupDate!!
                    to
                    groupAnimes
                        .sortedByDescending { it.rate?.status != null }
                        .sortedBy { it.rate?.status?.priority ?: Integer.MAX_VALUE }
            )
        }

        animes.forEach { animeWithRate ->
            val anime = animeWithRate.entity
            val isTodayPastItem =
                anime.nextEpisodeDate?.let { DateTime.now().isAfter(it) } ?: true

            val isSameDay = groupDate?.dayOfYear == anime.nextEpisodeDate?.dayOfYear

            if (!isSameDay || (todayPastFlag && !isTodayPastItem && groupAnimes.isNotEmpty())) {
                addGroup()
                groupAnimes.clear()
                todayPastFlag = false
            }

            groupAnimes.add(animeWithRate)
            groupDate = anime.nextEpisodeDate
        }

        if (groupAnimes.isNotEmpty()) {
            addGroup()
        }

        return groupList
    }

    data class Params(val filter: String?)
}

typealias Calendar = List<Pair<DateTime, List<AnimeWithRate>>>