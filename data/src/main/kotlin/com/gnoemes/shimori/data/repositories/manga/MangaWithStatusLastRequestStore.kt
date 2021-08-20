package com.gnoemes.shimori.data.repositories.manga

import com.gnoemes.shimori.data.daos.LastRequestDao
import com.gnoemes.shimori.data.repositories.lastrequests.GroupLastRequestStore
import com.gnoemes.shimori.model.app.Request
import javax.inject.Inject

class MangaWithStatusLastRequestStore @Inject constructor(
    dao : LastRequestDao
) : GroupLastRequestStore(Request.MANGAS_WITH_STATUS, dao)