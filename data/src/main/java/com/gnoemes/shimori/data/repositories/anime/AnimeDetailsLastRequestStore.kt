package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.data.core.database.daos.LastRequestDao
import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore

class AnimeDetailsLastRequestStore(
    dao : LastRequestDao
) : GroupLastRequestStore(Request.ANIME_DETAILS, dao)