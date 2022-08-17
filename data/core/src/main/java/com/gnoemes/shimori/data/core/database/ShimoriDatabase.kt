package com.gnoemes.shimori.data.core.database

import com.gnoemes.shimori.data.core.database.daos.*

interface ShimoriDatabase {
    val rateDao: RateDao
    val rateSortDao: RateSortDao
    val userDao: UserDao
    val lastRequestDao: LastRequestDao
    val animeDao: AnimeDao
    val mangaDao: MangaDao
    val ranobeDao: RanobeDao
    val listPinDao : ListPinDao
    val rateToSyncDao : RateToSyncDao
}