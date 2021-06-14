package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.calendar.CalendarRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCalendar @Inject constructor(
    private val repository: CalendarRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateCalendar.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.forceRefresh || repository.needUpdateCalendar()) {
                repository.updateCalendar()
            }
        }
    }

    data class Params(val forceRefresh: Boolean)
}