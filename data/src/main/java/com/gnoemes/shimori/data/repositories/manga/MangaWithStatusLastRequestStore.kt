package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.data.base.database.daos.LastRequestDao
import com.gnoemes.shimori.data.base.entities.app.Request
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore

class MangaWithStatusLastRequestStore(
    dao: LastRequestDao
) : GroupLastRequestStore(Request.MANGAS_WITH_STATUS, dao)