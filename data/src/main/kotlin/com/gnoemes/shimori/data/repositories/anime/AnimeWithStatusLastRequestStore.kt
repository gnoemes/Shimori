package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.data.daos.LastRequestDao
import com.gnoemes.shimori.data.repositories.lastrequests.GroupLastRequestStore
import com.gnoemes.shimori.model.app.Request
import javax.inject.Inject

class AnimeWithStatusLastRequestStore @Inject constructor(
    dao : LastRequestDao
) : GroupLastRequestStore(Request.ANIMES_WITH_STATUS, dao)