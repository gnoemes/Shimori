package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.data.core.database.daos.LastRequestDao
import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore

class AnimeRolesLastRequestStore(
    dao : LastRequestDao
) : GroupLastRequestStore(Request.ANIME_ROLES, dao)