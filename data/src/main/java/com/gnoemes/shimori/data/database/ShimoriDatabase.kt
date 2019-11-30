package com.gnoemes.shimori.data.database

import com.gnoemes.shimori.data.daos.AnimeDao
import com.gnoemes.shimori.data.daos.RateDao

interface ShimoriDatabase {
    fun animeDao(): AnimeDao
    fun rateDao() : RateDao
}