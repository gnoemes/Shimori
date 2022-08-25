package com.gnoemes.shimori.data.repositories.ranobe

import com.gnoemes.shimori.data.core.database.daos.LastRequestDao
import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore

class RanobeDetailsLastRequestStore(
    dao : LastRequestDao
) : GroupLastRequestStore(Request.RANOBE_DETAILS, dao)