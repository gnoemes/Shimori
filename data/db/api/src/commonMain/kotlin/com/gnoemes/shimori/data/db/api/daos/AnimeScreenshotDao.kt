package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import kotlinx.coroutines.flow.Flow

interface AnimeScreenshotDao : EntityDao<AnimeScreenshot> {
    suspend fun sync(titleId: Long, remote: List<AnimeScreenshot>)

    fun observeByTitleId(id: Long): Flow<List<AnimeScreenshot>>
}