package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo
import kotlinx.coroutines.flow.Flow

abstract class AnimeVideoDao : EntityDao<AnimeVideo>() {
    abstract suspend fun sync(titleId: Long, remote: List<AnimeVideo>)

    abstract fun observeByTitleId(id: Long): Flow<List<AnimeVideo>>
}