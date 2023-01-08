package com.gnoemes.shimori.data.repositories.track

import com.gnoemes.shimori.data.core.database.daos.LastRequestDao
import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore

class SyncPendingTracksLastRequestStore(
    dao: LastRequestDao
) : GroupLastRequestStore(Request.SYNC_PENDING_TRACKS, dao)