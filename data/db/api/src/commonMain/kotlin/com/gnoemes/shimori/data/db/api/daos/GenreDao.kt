package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

abstract class GenreDao : EntityDao<Genre> {
    abstract fun queryAll(): List<Genre>
    abstract fun queryById(id: Long): Genre?
    abstract fun queryByTitle(id: Long, type: TrackTargetType, sourceId: Long): List<Genre>
    abstract fun countBySource(sourceId: Long): Long
    abstract fun queryBySource(sourceId: Long): List<Genre>
    abstract fun observeByTitle(id: Long, type: TrackTargetType, sourceId: Long): Flow<List<Genre>>
}
