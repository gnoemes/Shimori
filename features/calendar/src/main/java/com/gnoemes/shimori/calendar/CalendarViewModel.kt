package com.gnoemes.shimori.calendar

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.common.utils.ObservableLoadingCounter
import com.gnoemes.common.utils.collectFrom
import com.gnoemes.shimori.domain.interactors.UpdateCalendar
import com.gnoemes.shimori.domain.observers.ObserveCalendar
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class CalendarViewModel @AssistedInject constructor(
    @Assisted initialState: CalendarViewState,
    private val observeCalendar: ObserveCalendar,
    private val updateCalendar: UpdateCalendar
) : BaseViewModel<CalendarViewState>(initialState) {
    private val searchQuery = ConflatedBroadcastChannel<String>()
    private val updateLoadingState = ObservableLoadingCounter()

    init {
        viewModelScope.launch {
            updateLoadingState.observable.collect { loading ->
                setState { copy(refreshing = loading) }
            }
        }

        viewModelScope.launch {
            observeCalendar.observe()
                .collect {
                    setState {
                        copy(results = it.map {
                            CalendarItem(it.first, it.second)
                        })
                    }
                }
        }

        viewModelScope.launch {
            searchQuery.asFlow()
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val filter = if (query.isBlank()) null else query
                    setState { copy(query = filter) }
                }
        }

        selectSubscribe(CalendarViewState::query) { query ->
            observeCalendar(ObserveCalendar.Params(
                    filter = query
            ))
        }
        observeCalendar(ObserveCalendar.Params(null))

        refresh(false)
    }

    fun refresh() {
        refresh(true)
    }

    fun setSearchQuery(newText: String) {
        searchQuery.sendBlocking(newText)
    }

    private fun refresh(force: Boolean) {
        updateCalendar(UpdateCalendar.Params(force)).also {
            viewModelScope.launch {
                updateLoadingState.collectFrom(it)
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: CalendarViewState): CalendarViewModel
    }

    companion object : MvRxViewModelFactory<CalendarViewModel, CalendarViewState> {
        override fun create(viewModelContext: ViewModelContext, state: CalendarViewState): CalendarViewModel? {
            val fragment: CalendarFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
