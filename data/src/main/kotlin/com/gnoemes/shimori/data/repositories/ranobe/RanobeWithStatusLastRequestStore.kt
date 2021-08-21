package com.gnoemes.shimori.data.repositories.ranobe

import com.gnoemes.shimori.data.daos.LastRequestDao
import com.gnoemes.shimori.data.repositories.lastrequests.GroupLastRequestStore
import com.gnoemes.shimori.model.app.Request
import javax.inject.Inject

class RanobeWithStatusLastRequestStore @Inject constructor(
    dao : LastRequestDao
) : GroupLastRequestStore(Request.RANOBE_WITH_STATUS, dao)