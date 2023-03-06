package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import kotlinx.coroutines.flow.Flow

abstract class AnimeScreenshotDao : EntityDao<AnimeScreenshot>() {
    abstract suspend fun sync(titleId: Long, remote: List<AnimeScreenshot>)

    abstract fun observeByTitleId(id: Long): Flow<List<AnimeScreenshot>>
}