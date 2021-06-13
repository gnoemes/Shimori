package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.di.ProcessLifetime
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.calendar.CalendarRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateCalendar @Inject constructor(
    private val repository: CalendarRepository,
    dispatcher: AppCoroutineDispatchers,
    @ProcessLifetime val processScope: CoroutineScope
) : Interactor<UpdateCalendar.Params>() {
    override val scope: CoroutineScope = processScope + dispatcher.io

    override suspend fun doWork(params: Params) {
        if (params.forceRefresh || repository.needUpdateCalendar()) {
            repository.updateCalendar()
        }
    }

    data class Params(val forceRefresh: Boolean)
}