package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import kotlinx.coroutines.flow.Flow

interface AnimeScreenshotDao : EntityDao<AnimeScreenshot> {
    fun queryByTitleId(id: Long): List<AnimeScreenshot>
    fun observeByTitleId(id: Long): Flow<List<AnimeScreenshot>>
}