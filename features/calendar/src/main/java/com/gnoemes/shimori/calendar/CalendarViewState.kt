package com.gnoemes.shimori.calendar

import com.airbnb.mvrx.MvRxState

data class CalendarViewState(
    val query: String? = null,
    val results: List<CalendarItem>? = null,
    val refreshing : Boolean = false
) : MvRxState