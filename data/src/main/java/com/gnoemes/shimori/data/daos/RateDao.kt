package com.gnoemes.shimori.data.daos

import androidx.room.Dao
import com.gnoemes.shimori.model.rate.Rate

@Dao
abstract class RateDao : EntityDao<Rate> {

}