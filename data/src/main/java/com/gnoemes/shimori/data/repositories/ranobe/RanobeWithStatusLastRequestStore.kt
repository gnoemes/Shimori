package com.gnoemes.shimori.data.repositories.ranobe

import com.gnoemes.shimori.data.base.database.daos.LastRequestDao
import com.gnoemes.shimori.data.base.entities.app.Request
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore

class RanobeWithStatusLastRequestStore(
    dao: LastRequestDao
) : GroupLastRequestStore(Request.RANOBE_WITH_STATUS, dao)