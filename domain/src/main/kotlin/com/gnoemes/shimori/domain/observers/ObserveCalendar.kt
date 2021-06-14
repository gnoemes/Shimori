package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.calendar.CalendarRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.anime.AnimeWithRate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCalendar @Inject constructor(
    private val repository: CalendarRepository,
) : SubjectInteractor<ObserveCalendar.Params, List<AnimeWithRate>>() {

    override fun createObservable(params: Params): Flow<List<AnimeWithRate>> {
        return repository.observeCalendar(params.filter?.lowercase())
    }

    data class Params(val filter: String?)
}
