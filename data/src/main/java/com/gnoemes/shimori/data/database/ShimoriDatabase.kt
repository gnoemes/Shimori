package com.gnoemes.shimori.data.database

import com.gnoemes.shimori.data.daos.AnimeDao
import com.gnoemes.shimori.data.daos.LastRequestDao
import com.gnoemes.shimori.data.daos.RateDao
import com.gnoemes.shimori.data.daos.UserDao

interface ShimoriDatabase {
    fun animeDao(): AnimeDao
    fun rateDao(): RateDao
    fun lastRequestDao(): LastRequestDao
    fun userDao(): UserDao
}