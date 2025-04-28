package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.common.Studio
import kotlinx.coroutines.flow.Flow

interface StudioDao : EntityDao<Studio> {
    fun queryById(id: Long): Studio?
    fun queryBySource(sourceId: Long): List<Studio>
    fun countBySource(sourceId: Long): Long
    fun queryByTitle(targetId: Long, sourceId: Long): List<Studio>
    fun observeByTitle(targetId: Long, sourceId: Long): Flow<List<Studio>>
}