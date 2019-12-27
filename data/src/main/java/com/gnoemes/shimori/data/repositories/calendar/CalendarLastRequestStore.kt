package com.gnoemes.shimori.data.repositories.calendar

import com.gnoemes.shimori.data.daos.LastRequestDao
import com.gnoemes.shimori.data.repositories.lastrequests.GroupLastRequestStore
import com.gnoemes.shimori.model.app.Request
import javax.inject.Inject

class CalendarLastRequestStore @Inject constructor(
    dao: LastRequestDao
) : GroupLastRequestStore(Request.CALENDAR, dao)