package com.gnoemes.shimori.data.database

import com.gnoemes.shimori.data.daos.*

interface ShimoriDatabase {
    fun animeDao(): AnimeDao
    fun rateDao(): RateDao
    fun lastRequestDao(): LastRequestDao
    fun userDao(): UserDao
    fun rateSortDao() : RateSortDao
}