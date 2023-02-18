package com.gnoemes.shimori.data.core.database

import com.gnoemes.shimori.data.core.database.daos.*

interface ShimoriDatabase {
    val trackDao: TrackDao
    val listSortDao: ListSortDao
    val userDao: UserDao
    val lastRequestDao: LastRequestDao
    val animeDao: AnimeDao
    val animeVideoDao : AnimeVideoDao
    val mangaDao: MangaDao
    val ranobeDao: RanobeDao
    val listPinDao: ListPinDao
    val trackToSyncDao: TrackToSyncDao
    val characterDao: CharacterDao
    val sourceIdsSyncDao: SourceIdsSyncDao
}