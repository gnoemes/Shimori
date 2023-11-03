package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import kotlinx.coroutines.flow.Flow

interface AnimeVideoDao : EntityDao<AnimeVideo> {
    suspend fun sync(titleId: Long, remote: List<AnimeVideo>)

    fun observeByTitleId(id: Long): Flow<List<AnimeVideo>>
}