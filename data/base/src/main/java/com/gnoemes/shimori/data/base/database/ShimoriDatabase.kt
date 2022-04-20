package com.gnoemes.shimori.data.base.database

import com.gnoemes.shimori.data.base.database.daos.*

interface ShimoriDatabase {
    val rateDao: RateDao
    val rateSortDao: RateSortDao
    val userDao: UserDao
    val lastRequestDao: LastRequestDao
    val animeDao: AnimeDao
    val mangaDao: MangaDao
    val ranobeDao: RanobeDao
}