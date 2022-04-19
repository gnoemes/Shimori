package com.gnoemes.shimori.data.base.database

import com.gnoemes.shimori.data.base.database.daos.RateDao

interface ShimoriDatabase {
    val rateDao: RateDao
}