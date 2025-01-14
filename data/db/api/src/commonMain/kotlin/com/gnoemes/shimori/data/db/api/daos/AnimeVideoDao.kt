package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import kotlinx.coroutines.flow.Flow

interface AnimeVideoDao : EntityDao<AnimeVideo> {
    fun queryByTitleId(id: Long): List<AnimeVideo>
    fun observeByTitleId(id: Long): Flow<List<AnimeVideo>>
}